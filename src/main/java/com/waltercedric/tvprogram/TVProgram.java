package com.waltercedric.tvprogram;

import java.time.LocalTime;

public class TVProgram {

    private final String channel;
    private final String title;
    private final String category;
    private final String description;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TVProgram(String channel, String title, String category, String description, LocalTime startTime, LocalTime endTime) {
        this.channel = channel;
        this.title = title;
        this.category = category;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getChannel() {
        return channel;
    }

    public String getCategory() {
        return category;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
