package com.waltercedric.tvprogram.plugins.mapping;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TimeToFrenchTest {

    @Test
    public void with() {
        TimeToFrench timeToFrench = new TimeToFrench();

        assertThat(timeToFrench.convertTimeToText("12:12"), is("douze heure douze"));
        assertThat(timeToFrench.convertTimeToText("12:30"), is("douze heure et demi"));
        assertThat(timeToFrench.convertTimeToText("12:15"), is("douze heure et quart"));
        assertThat(timeToFrench.convertTimeToText("12:45"), is("treize heure moins le quart"));
        assertThat(timeToFrench.convertTimeToText("12:00"), is("douze heure pile"));
        assertThat(timeToFrench.convertTimeToText("23:45"), is("minuit moins le quart"));
        assertThat(timeToFrench.convertTimeToText("17:45"), is("dix huit heure moins le quart"));
        assertThat(timeToFrench.convertTimeToText("9:45"), is("dix heure moins le quart"));
    }

}