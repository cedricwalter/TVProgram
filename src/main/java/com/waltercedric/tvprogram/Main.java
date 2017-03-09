package com.waltercedric.tvprogram;

import com.waltercedric.tvprogram.guide.TVGuide;
import com.waltercedric.tvprogram.guide.TVGuideFromTo;
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;

import java.time.LocalTime;

class Main {

    public static void main(String[] args) throws Exception {

        Config config = new Config();

        TVProgramBuilder builder = config.getBuilder();

        LocalTime now = LocalTime.now();

//        TVGuide guideNow = new TVGuideNow(builder.getTodayProgram(), now, 10, 5);
//        new TVReader().read(guideNow);

        TVGuide guideFromTo = new TVGuideFromTo(builder.getTodayProgram(), LocalTime.parse(config.getFrom()), LocalTime.parse(config.getTo()));
        new TVReader().read(guideFromTo);

//        System.out.println(guideFromTo);
    }

}
