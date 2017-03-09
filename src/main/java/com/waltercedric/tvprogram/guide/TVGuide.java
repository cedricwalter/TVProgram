package com.waltercedric.tvprogram.guide;

import com.waltercedric.tvprogram.TVProgram;

import java.util.List;

public interface TVGuide {

    List<TVProgram> getProgram();

    String getIntroduction();

    String getForEachProgram();
}
