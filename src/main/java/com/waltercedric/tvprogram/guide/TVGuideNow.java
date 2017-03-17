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
package com.waltercedric.tvprogram.guide;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVProgram;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TVGuideNow implements TVGuide {

    private final List<TVProgram> programs;
    private final Config config;
    private final LocalTime now;

    public TVGuideNow(Config config, List<TVProgram> programs, LocalTime now) {
        this.programs = programs;
        this.now = now;
        this.config = config;
    }

    @Override
    public String toString() {
        return "TVGuideNow{" +
                ", config=" + config +
                ", now=" + now +
                "programs=" + programs +
                '}';
    }

    public List<TVProgram> getProgram() {
        List<TVProgram> programs = new ArrayList<>();
        for (TVProgram program : this.programs) {

            LocalTime startTime = program.getStartTime();
            LocalTime endTime = program.getEndTime();

            boolean add = startTime.isBefore(now) && now.isBefore(endTime);
            if (add) {
                long minutes = ChronoUnit.MINUTES.between(now, endTime);
                program.setRestTime(minutes);

                if (config.getFree().contains(program.getChannel())) {
                    programs.add(program);
                }
                if (config.usePremium()) {
                    if (config.getPremium().contains(program.getChannel())) {
                        programs.add(program);
                    }
                }
            }
        }

        return programs;
    }

    @Override
    public String getIntroduction() {
        return config.getSentenceNow_introduction();
    }

    @Override
    public String getForEachProgram() {
        return config.getSentenceNow_each();
    }

}
