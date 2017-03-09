package com.waltercedric.tvprogram;

import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;
import com.waltercedric.tvprogram.plugins.sources.Webnext;

import java.time.LocalTime;
import java.util.List;

class Main {

    public static void main(String[] args) throws Exception {
        TVProgramBuilder builder =  new Webnext();

        TVGuide guide = new TVGuide(builder.getTodayProgram());

        LocalTime now = LocalTime.now();

        List<TVProgram> programs = guide.getProgram(now, 10, 5);

        new TVReader().read(programs);
    }

}
