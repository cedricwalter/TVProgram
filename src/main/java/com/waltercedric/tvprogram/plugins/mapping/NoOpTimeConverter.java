package com.waltercedric.tvprogram.plugins.mapping;

public class NoOpTimeConverter implements TimeToTextConverter {
    @Override
    public String convertTimeToText(String value) {
        return value;
    }
}
