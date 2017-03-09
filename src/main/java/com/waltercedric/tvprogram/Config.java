package com.waltercedric.tvprogram;

import com.waltercedric.tvprogram.plugins.mapping.TimeToTextConverter;
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Config {

    private final String builder;
    private final String timeToTextConverter;
    private final String fromto_introduction;
    private final String fromto_each;
    private String sentenceNow_each;
    private String sentenceNow_introduction;
    private final String voice;
    private List<String> free;
    private List<String> premium;
    private boolean usePremium;

    public Config() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/config.properties");
        Properties props = new Properties();
        try {
            props.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException("Can not read config file");
        }
        premium = Arrays.asList(props.getProperty("premium").split(","));
        free = Arrays.asList(props.getProperty("free").split(","));
        usePremium = Boolean.valueOf(props.getProperty("use.premium"));
        voice = props.getProperty("voice");

        sentenceNow_introduction = props.getProperty("TVGuideNow.introduction");
        sentenceNow_each = props.getProperty("TVGuideNow.each");

        fromto_introduction = props.getProperty("TVGuideFromTo.introduction");
        fromto_each = props.getProperty("TVGuideFromTo.each");

        builder = props.getProperty("builder");
        timeToTextConverter = props.getProperty("TimeToTextConverter");
    }

    public TVProgramBuilder getBuilder() {
        try {
            return (TVProgramBuilder) Class.forName(builder).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("builder class is wrongly set", e);
        }
    }

    public TimeToTextConverter getTimeToTextConverter() {
        try {
            return (TimeToTextConverter) Class.forName(timeToTextConverter).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("builder class is wrongly set", e);
        }
    }

    public String getVoice() {
        return voice;
    }

    public List<String> getPremium() {
        return premium;
    }

    public boolean usePremium() {
        return usePremium;
    }

    public List<String> getFree() {
        return free;
    }

    public String getSentenceNow_each() {
        return sentenceNow_each;
    }

    public String getSentenceNow_introduction() {
        return sentenceNow_introduction;
    }

    public String getFromto_introduction() {
        return fromto_introduction;
    }

    public String getFromto_each() {
        return fromto_each;
    }

}
