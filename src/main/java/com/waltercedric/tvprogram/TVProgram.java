package com.waltercedric.tvprogram;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TVProgram {

    private final String channel;
    private final String title;
    private final String category;
    private final String description;
    private final LocalTime startTime;

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    private LocalTime endTime;



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

    public Long getDuration() {
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }

    @Override
    public String toString() {
        return "TVProgram{" +
                "channel='" + channel + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                "}\n";
    }
}
