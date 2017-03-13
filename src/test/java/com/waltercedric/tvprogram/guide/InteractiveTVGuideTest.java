package com.waltercedric.tvprogram.guide;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVProgram;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class InteractiveTVGuideTest {

    private TVProgram tvProgram1 = new TVProgram("channel1", "anyTitle", "anyCategory", "anyDescription", LocalTime.of(9, 5), LocalTime.of(9, 20));
    private TVProgram tvProgram2 = new TVProgram("channel2", "anyTitle", "anyCategory", "anyDescription", LocalTime.of(9, 5), LocalTime.of(9, 25));
    private TVProgram tvProgram3 = new TVProgram("channel3", "anyTitle", "anyCategory", "anyDescription", LocalTime.of(9, 5), LocalTime.of(9, 35));

    @Test
    public void withTVProgram_channelUp_expectTvProgram2() {
        // arrange
        Config config = mockConfigWith3Channel();

        List<TVProgram> list = getTVProgram();

        InteractiveTVGuide interactiveTVGuide = new InteractiveTVGuide(config, list, LocalTime.of(9, 10), 0);

        // act
        interactiveTVGuide.channelUp();

        //assert
        List<TVProgram> program = interactiveTVGuide.getProgram();
        assertThat(program.size(), is(1));
        assertThat(program.get(0), is(tvProgram2));
    }

    @Test
    public void withTVProgram_channelUpRepetition_expectTvProgram3() {
        // arrange
        Config config = mockConfigWith3Channel();

        List<TVProgram> list = getTVProgram();

        InteractiveTVGuide interactiveTVGuide = new InteractiveTVGuide(config, list, LocalTime.of(9, 10), 0);

        // act
        interactiveTVGuide.channelUp();
        interactiveTVGuide.channelUp();


        //assert
        List<TVProgram> program = interactiveTVGuide.getProgram();
        assertThat(program.size(), is(1));
        assertThat(program.get(0), is(tvProgram3));
    }

    @Test
    public void withTVProgram_channelUpRepetitionChannelDown_expectTvProgram2() {
        // arrange
        Config config = mockConfigWith3Channel();

        List<TVProgram> list = getTVProgram();

        InteractiveTVGuide interactiveTVGuide = new InteractiveTVGuide(config, list, LocalTime.of(9, 10), 0);

        // act
        interactiveTVGuide.channelUp();  //bad to use up to test down
        interactiveTVGuide.channelUp();
        interactiveTVGuide.channelDown();

        //assert
        List<TVProgram> program = interactiveTVGuide.getProgram();
        assertThat(program.size(), is(1));
        assertThat(program.get(0), is(tvProgram2));
    }

    @Test
    public void withTVProgram_nowIsLate_expectNoTVProgram() {
        // arrange
        Config config = mockConfigWith3Channel();

        List<TVProgram> list = getTVProgram();

        InteractiveTVGuide interactiveTVGuide = new InteractiveTVGuide(config, list, LocalTime.of(10, 0), 0);

        // act
        List<TVProgram> program = interactiveTVGuide.getProgram();

        //assert
        assertThat(program.size(), is(0));
    }

    @Test
    public void withTVProgram_nowIsLateButTimeDown_expectTVProgram1OnChannel1() {
        // arrange
        Config config = mockConfigWith3Channel();

        List<TVProgram> list = getTVProgram();

        InteractiveTVGuide interactiveTVGuide = new InteractiveTVGuide(config, list, LocalTime.of(9, 30), 0);

        // act
        interactiveTVGuide.timeDown(); // channel1 is still set

        //assert
        List<TVProgram> program = interactiveTVGuide.getProgram();
        assertThat(program.size(), is(1));
        assertThat(program.get(0), is(tvProgram1));
    }

    private List<TVProgram> getTVProgram() {
        List<TVProgram> list = new ArrayList<>();
        list.add(tvProgram1);
        list.add(tvProgram2);
        list.add(tvProgram3);

        return list;
    }

    private Config mockConfigWith3Channel() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getFree()).thenReturn(Arrays.asList("channel1", "channel2", "channel3"));
        Mockito.when(config.getChannels()).thenReturn(Arrays.asList("channel1", "channel2", "channel3"));

        return config;
    }

}