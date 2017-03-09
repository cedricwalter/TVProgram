package com.waltercedric.tvprogram.plugins.mapping;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TimeToEnglishTest {

    @Test
    public void with() {
        TimeToEnglish timeToEnglish = new TimeToEnglish();
        String text = timeToEnglish.convertTimeToText("1:15");

        assertThat(text, is("Twelve past Twelve"));
    }

}