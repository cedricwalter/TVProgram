package com.waltercedric.tvprogram;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class TVGuide {

    private final List<TVProgram> programs;
    private final Config config = new Config();

    public TVGuide(List<TVProgram> programs) {
        this.programs = programs;
    }

    public List<TVProgram> getProgram(LocalTime now, int minutesToAdd, int minutesToSubtract) {
        List<TVProgram> programs = new ArrayList<>();
        for (TVProgram program : this.programs) {
            if (program.getStartTime().isBefore(now.plusMinutes(minutesToAdd)) &&
                    program.getStartTime().isAfter(now.minusMinutes(minutesToSubtract))) {

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

}
