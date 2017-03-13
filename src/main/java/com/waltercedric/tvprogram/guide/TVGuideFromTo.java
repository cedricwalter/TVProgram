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
package com.waltercedric.tvprogram.guide;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVProgram;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class TVGuideFromTo implements TVGuide {

    private final List<TVProgram> programs;
    private final LocalTime from;
    private final LocalTime to;
    private final Config config;

    public TVGuideFromTo(Config config, List<TVProgram> programs, LocalTime from, LocalTime to) {
        this.programs = programs;
        this.from = from;
        this.to = to;
        this.config = config;
    }

    @Override
    public List<TVProgram> getProgram() {
        List<TVProgram> programs = new ArrayList<>();
        for (TVProgram program : this.programs) {
            boolean startingAtTime = program.getStartTime().isAfter(from);
            boolean startingBeforeEnd = program.getStartTime().isBefore(to);
            if (startingBeforeEnd && startingAtTime) {

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
        return config.getFromto_introduction();
    }

    @Override
    public String getForEachProgram() {
        return config.getFromto_each();
    }

    @Override
    public String toString() {
        return "TVGuideFromTo{" +
                "programs=" + programs +
                ", from=" + from +
                ", to=" + to +
                ", config=" + config +
                "} \n";
    }
}
