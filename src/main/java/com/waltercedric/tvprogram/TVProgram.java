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
package com.waltercedric.tvprogram;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TVProgram {

    private final String channel;
    private final String title;
    private final String category;
    private final String description;
    private final LocalTime startTime;
    private long restTime;
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

    public long getRestTime() {
        return restTime;
    }

    public void setRestTime(long restTime) {
        this.restTime = restTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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
                ", restTime=" + restTime +
                "}\n";
    }
}
