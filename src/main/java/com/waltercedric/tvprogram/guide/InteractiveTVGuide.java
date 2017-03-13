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
        this.timeCursor = this.timeCursor.plusMinutes(30);

        return this.timeCursor;
    }

    public LocalTime timeDown() {
        this.timeCursor = this.timeCursor.minusMinutes(30);

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
