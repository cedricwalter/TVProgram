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
