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

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CircularListTest {

    @Test
    public void withCircularList_get_expectValues() {
        // arrange
        CircularList<String> strings = new CircularList<>();
        strings.addAll(Arrays.asList("A", "B", "C", "D"));

        // act + assert
        assertThat(strings.get(0), is("A"));
        assertThat(strings.get(1), is("B"));
        assertThat(strings.get(2), is("C"));
        assertThat(strings.get(3), is("D"));
        assertThat(strings.get(4), is("A"));
        assertThat(strings.get(5), is("B"));
        assertThat(strings.get(-2), is("C"));
    }

}