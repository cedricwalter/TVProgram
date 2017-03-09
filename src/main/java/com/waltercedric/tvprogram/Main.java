package com.waltercedric.tvprogram;

import com.waltercedric.tvprogram.guide.TVGuideNow;
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;

import java.time.LocalTime;
import java.util.List;

class Main {

    public static void main(String[] args) throws Exception {

        Config config = new Config();

        TVProgramBuilder builder = config.getBuilder();

        TVGuideNow guide = new TVGuideNow(builder.getTodayProgram());

        LocalTime now = LocalTime.now();

        List<TVProgram> programs = guide.getProgram(now, 10, 5);

        new TVReader().read(programs);
    }

}
