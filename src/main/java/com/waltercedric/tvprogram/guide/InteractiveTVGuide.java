/**
 * Copyright (c) 2017-2017 by Cédric Walter - www.cedricwalter.com
 *
 * TVProgram is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TVProgram is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.waltercedric.tvprogram.guide;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVProgram;
import com.waltercedric.tvprogram.utils.CircularList;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InteractiveTVGuide implements TVGuide {

    private final List<TVProgram> programs;
    private final Config config;
    private final CircularList<String> channels;

    private LocalTime timeCursor;
    private int channelCursor;

    public InteractiveTVGuide(Config config, List<TVProgram> programs, LocalTime now, int channelCursor) {
        this.programs = programs;
        this.config = config;

        this.timeCursor = now;
        this.channelCursor = channelCursor;

        this.channels = new CircularList<>();
        channels.addAll(config.getChannels());
    }

    public LocalTime timeUp() {
        this.timeCursor = this.timeCursor.plusMinutes(config.getTimeIncrement());

        return this.timeCursor;
    }

    public LocalTime timeDown() {
        this.timeCursor = this.timeCursor.minusMinutes(config.getTimeIncrement());

        return this.timeCursor;
    }

    public int channelUp() {
        this.channelCursor += 1;

        return this.channelCursor;
    }

    public int channelDown() {
        this.channelCursor -= 1;

        return this.channelCursor;
    }

    public List<TVProgram> getProgram() {
        // filter by time cursor
        TVGuideNow tvGuideNow = new TVGuideNow(config, programs, timeCursor);

        // filter by selected channel
        List<TVProgram> programOnChannelAtThatTime = new ArrayList<>();
        String desiredChannel = channels.get(channelCursor);

        for (TVProgram program : tvGuideNow.getProgram()) {
            if (program.getChannel().equals(desiredChannel)) {
                programOnChannelAtThatTime.add(program);
            }
        }

        return programOnChannelAtThatTime;
    }

    @Override
    public String getIntroduction() {
        return config.getInteractiveTVGuide_introduction();
    }

    @Override
    public String getForEachProgram() {
        return config.getInteractiveTVGuide_each();
    }

    public LocalTime getTimeCursor() {
        return timeCursor;
    }
}
