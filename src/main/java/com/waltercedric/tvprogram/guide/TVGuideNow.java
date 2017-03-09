package com.waltercedric.tvprogram.guide;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVProgram;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TVGuideNow implements TVGuide {

    private final List<TVProgram> programs;
    private final Config config = new Config();

    public TVGuideNow(List<TVProgram> programs) {
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
