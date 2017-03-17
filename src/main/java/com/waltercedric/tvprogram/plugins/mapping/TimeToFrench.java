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

public class TimeToFrench implements TimeToTextConverter {

    public String convertTimeToText(String value) {
        String[] split = value.split("\\:");

        return getTimeName(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
    }

    private String getTimeName(int hours, int minutes) {
        String time_name;

        if ((hours >= 1 && hours <= 23) && (minutes >= 0 && minutes <= 59)) {
            String hour_mint[] = {"", "Une", "deux", "trois", "quatre", "cinq", "Six", "sept", "huit", "neuf", "dix",
                    "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix sept", "dix huit", "dix neuf",
                    "vingt", "vingt un", "vingt deux", "vingt trois", "vingt quatre", "vingt cinq",
                    "vingt six", "vingt sept", "vingt huit", "vingt neuf"};

            String a;
            if (hours == 12) {
                a = hour_mint[13];// put 'one' if hour is 12
            } else {
                if (hours == 23) {
                    a = "minuit";
                } else {
                    a = hour_mint[hours + 1]; // if hour is not 12 then store an hour ahead of given hour
                }
            }

            if (minutes == 0) {
                time_name = hour_mint[hours] + " heure pile";
            } else if (minutes == 15) {
                time_name = hour_mint[hours] + " heure et quart";
            } else if (minutes == 30) {
                time_name = hour_mint[hours] + " heure et demi";
            } else if (minutes == 45) {
                if (hours == 23) {
                    time_name = a + " moins le quart";
                } else {
                    time_name = a + " heure moins le quart";
                }
            } else if (minutes < 30) {// for minutes between 1-29
                time_name = hour_mint[minutes] + " heure " + hour_mint[hours];
            } else {
                // between 31-59
                time_name = hour_mint[60 - minutes] + " to " + a;
            }
        } else {
            time_name = "invalid time format";
        }

        return time_name;
    }

}
