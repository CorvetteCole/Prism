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
package com.helion3.prism.api.results;


/**
 * Represents a complete copy of event record data from
 * a query result. Used for displaying individual entries
 * or for non-lookup actions.
 *
 */
public class ResultRecordComplete extends ResultRecord {
//    /**
//     * Converts location data view to a Location.
//     * @return Optional Location.
//     */
//    public Optional<Location> getLocation() {
//        Location location = null;
//
//        // @todo should be UUID, which isn't serialized by sponge (yet?)
//        Optional<String> optionalWorldId = data.getString(DataQuery.of("location", "WorldName"));
//        Optional<Integer> x = data.getInt(DataQuery.of("location", "x"));
//        Optional<Integer> y = data.getInt(DataQuery.of("location", "y"));
//        Optional<Integer> z = data.getInt(DataQuery.of("location", "z"));
//
//        if (optionalWorldId.isPresent()) {
//            Optional<World> optionalWorld = Prism.getGame().getServer().getWorld(optionalWorldId.get());
//            if (optionalWorld.isPresent() && x.isPresent() && y.isPresent() && z.isPresent()) {
//                location = new Location(optionalWorld.get(), x.get(), y.get(), z.get());
//            }
//        }
//
//        return Optional.fromNullable(location);
//    }
}