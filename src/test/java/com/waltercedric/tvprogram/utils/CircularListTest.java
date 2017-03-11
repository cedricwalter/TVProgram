package com.waltercedric.tvprogram.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CircularListTest {

    @Test
    public void withCircularList_get_expectValues() {
        // arrange
        CircularList<String> strings = new CircularList<>();
        strings.addAll(Arrays.asList("A", "B", "C", "D"));

        // act + assert
        assertThat(strings.get(0), is("A"));
        assertThat(strings.get(1), is("B"));
        assertThat(strings.get(2), is("C"));
        assertThat(strings.get(3), is("D"));
        assertThat(strings.get(4), is("A"));
        assertThat(strings.get(5), is("B"));
        assertThat(strings.get(-2), is("C"));
    }

}