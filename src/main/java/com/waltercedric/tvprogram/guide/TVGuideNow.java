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
