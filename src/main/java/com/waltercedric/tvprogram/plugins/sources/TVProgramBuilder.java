package com.waltercedric.tvprogram.plugins.sources;

import java.util.List;
import com.waltercedric.tvprogram.TVProgram;

public interface TVProgramBuilder {

    List<TVProgram> getTodayProgram();
}
