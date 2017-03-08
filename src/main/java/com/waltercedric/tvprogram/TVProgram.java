package com.waltercedric.tvprogram;

import org.jsoup.Jsoup;

import java.time.LocalTime;

public class TVProgram {

    private final String channel;
    private final String title;
    private final String category;
    private final String description;
    private final LocalTime startTime;

    public TVProgram(String title, String category, String description) {
        this.category = Jsoup.parse(category).text();
        this.description = Jsoup.parse(description).text();

        String text = Jsoup.parse(title).text();
        //TODO create an extractor for specific webnext.fr
        String[] split = text.split("\\|");
        this.title = split[2].trim();
        this.startTime = LocalTime.parse(split[1].trim() + ":00");
        this.channel = split[0].trim();
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
}
