package com.waltercedric.tvprogram.guide;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVProgram;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TVGuideNow implements TVGuide {

    private final List<TVProgram> programs;
    private final Config config = new Config();
    private LocalTime now;
    private int minutesToAdd;
    private int minutesToSubtract;

    public TVGuideNow(List<TVProgram> programs, LocalTime now, int minutesToAdd, int minutesToSubtract) {
        this.programs = programs;
        this.now = now;
        this.minutesToAdd = minutesToAdd;
        this.minutesToSubtract = minutesToSubtract;
    }

    @Override
    public String toString() {
        return "TVGuideNow{" +
                ", config=" + config +
                ", now=" + now +
                ", + minutes=" + minutesToAdd +
                ", - minutes=" + minutesToSubtract +
                "programs=" + programs +
                '}';
    }

    public List<TVProgram> getProgram() {
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

    @Override
    public String getIntroduction() {
        return config.getSentenceNow_introduction();
    }

    @Override
    public String getForEachProgram() {
        return config.getSentenceNow_each();
    }

}
