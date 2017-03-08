package com.waltercedric.tvprogram;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

class Config {

    private String sentenceNow;
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
        sentenceNow = props.getProperty("sentenceNow");
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

    public String getSentenceNow() {
        return sentenceNow;
    }
}
