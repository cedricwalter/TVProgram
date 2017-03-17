/**
 * Copyright (c) 2017-2017 by Cédric Walter - www.cedricwalter.com
 *
 * TVProgram is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TVProgram is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.waltercedric.tvprogram.plugins.mapping;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TimeToFrenchTest {

    @Test
    public void with() {
        TimeToFrench timeToFrench = new TimeToFrench();

        assertThat(timeToFrench.convertTimeToText("12:12"), is("douze heure douze"));
        assertThat(timeToFrench.convertTimeToText("12:30"), is("douze heure et demi"));
        assertThat(timeToFrench.convertTimeToText("12:15"), is("douze heure et quart"));
        assertThat(timeToFrench.convertTimeToText("12:45"), is("treize heure moins le quart"));
        assertThat(timeToFrench.convertTimeToText("12:00"), is("douze heure pile"));
        assertThat(timeToFrench.convertTimeToText("23:45"), is("minuit moins le quart"));
        assertThat(timeToFrench.convertTimeToText("17:45"), is("dix huit heure moins le quart"));
        assertThat(timeToFrench.convertTimeToText("9:45"), is("dix heure moins le quart"));
    }

}