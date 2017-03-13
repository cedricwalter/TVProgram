/**
 * Copyright (c) 2017-2017 by Cédric Walter - www.cedricwalter.com
 * <p>
 * TVProgram is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * TVProgram is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.waltercedric.tvprogram.utils;

import java.util.ArrayList;

// do not work for mutable list, only support ArrayList, used only for channels
public class CircularList<E> extends ArrayList<E> {

    @Override
    public E get(int index) {

        int listSize = super.size();
        int indexToGet = index % listSize;

        //might happen to be negative
        indexToGet = (indexToGet < 0) ? indexToGet + listSize : indexToGet;

        return super.get(indexToGet);
    }
}