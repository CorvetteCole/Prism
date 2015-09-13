/**
 * This file is part of Prism, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015 Helion3 http://helion3.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.helion3.prism.api.records;

import java.util.Date;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTransaction;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;

import static com.google.common.base.Preconditions.checkNotNull;

import com.helion3.prism.queues.RecordingQueue;
import com.helion3.prism.records.EventSource;

/**
 * An easy-to-understand factory class for Prism {@link EventRecord}s.
 *
 * By chaining methods together, you can build a record with
 * natural-language style syntax.
 *
 * For example:
 *
 * new PrismRecord().player(player).brokeBlock(blockLoc).save()
 *
 */
public class PrismRecord {
    private String eventName;
    private EventSource source;
    private DataContainer data = new MemoryDataContainer();

    /**
     * Describe the Player responsible for the event this
     * record describes.
     *
     * @param player Player responsible for this event
     * @return PrismRecord
     */
    public PrismRecord player(Player player){
        this.source = new EventSource(player);
        return this;
    }

    /**
     * Helper method for writing block transaction data, using only
     * the final replacement value.
     *
     * @param transaction
     */
    private void writeBlockTransaction(BlockTransaction transaction) {
        checkNotNull(transaction);

        // Need a little prep, since location has redundant block info for our concerns
        DataContainer location = transaction.getOriginal().getLocation().get().toContainer();
        location.remove(new DataQuery("BlockType"));

        data.set(new DataQuery("location"), location);

        // Storing the state only, so we don't also get location
        data.set(new DataQuery("original"), transaction.getOriginal().getState());
        data.set(new DataQuery("replacement"), transaction.getFinalReplacement().getState());
    }

    /**
     * Describes a single block break at a given Location.
     *
     * @param transaction Block broken.
     * @return PrismRecord
     */
    public PrismRecord brokeBlock(BlockTransaction transaction){
        this.eventName = "block-break";
        writeBlockTransaction(transaction);
        return this;
    }

    /**
     * Describes a single block place at a given Location.
     *
     * @param transaction Block placed.
     * @return PrismRecord
     */
    public PrismRecord placedBlock(BlockTransaction transaction){
        this.eventName = "block-place";
        writeBlockTransaction(transaction);
        return this;
    }

    /**
     * Describes which block was replaced by a block action.
     * @param snapshot BlockSnapshot Snapshot of the former block
     * @return PrismRecord
     */
    public PrismRecord replacing(BlockSnapshot snapshot) {
        checkNotNull(snapshot);
        data.set(new DataQuery("state"), snapshot.getState().toContainer());
        return this;
    }

    /**
     * Describes a single location an event occurred at.
     * @param location Location of event
     * @return PrismRecord
     */
    public PrismRecord at(Location<?> location) {
        data.set(new DataQuery("location"), location.toContainer());
        return this;
    }

    /**
     * Describes a player join.
     * @return
     */
    public PrismRecord joined() {
        this.eventName = "player-join";
        return this;
    }

    /**
     * Describes a player quit.
     * @return
     */
    public PrismRecord quit() {
        this.eventName = "player-quit";
        return this;
    }

    /**
     * Build the final event record and send it to the queue.
     */
    public void save(){
        // Validation
        if (source == null) {
            throw new IllegalArgumentException("Event record can not be created - invalid source.");
        }
        else if (eventName == null) {
            throw new IllegalArgumentException("Event record can not be created - invalid event name.");
        }

        data.set(new DataQuery("eventName"), eventName);
        data.set(new DataQuery("created"), new Date());
        data.set(new DataQuery("source"), source.getSourceIdentifier());

        // Source
        String sourceKey = source.isPlayer() ? "player" : "source";
        data.set(new DataQuery(sourceKey), source.getSourceIdentifier());

        // Queue the finished record for saving
        RecordingQueue.add(data);
    }
}