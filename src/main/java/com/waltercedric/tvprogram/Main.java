package com.waltercedric.tvprogram;

import com.waltercedric.tvprogram.guide.TVGuide;
import com.waltercedric.tvprogram.guide.TVGuideFromTo;
import com.waltercedric.tvprogram.guide.TVGuideNow;
import com.waltercedric.tvprogram.plugins.reader.TVReader;
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;

import java.time.LocalTime;

class Main {

    public static void main(String[] args) throws Exception {
        Config config = new Config();

        TVProgramBuilder builder = config.getBuilder();
        if ("now".equals(args[0])) {
            LocalTime now = LocalTime.now();
            TVGuide guideNow = new TVGuideNow(builder.getTodayProgram(), now, Integer.valueOf(args[1]), Integer.valueOf(args[2]));
            new TVReader().read(guideNow);
        } else if ("program".equals(args[0])) {
            TVGuide guideFromTo = new TVGuideFromTo(builder.getTodayProgram(), LocalTime.parse(args[1]), LocalTime.parse(args[2]));
            new TVReader().read(guideFromTo);
        }
    }

}