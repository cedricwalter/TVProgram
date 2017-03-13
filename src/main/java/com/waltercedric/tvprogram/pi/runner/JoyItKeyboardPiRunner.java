package com.waltercedric.tvprogram.pi.runner;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVReader;
import com.waltercedric.tvprogram.guide.InteractiveTVGuide;
import com.waltercedric.tvprogram.pi.drivers.JoyItKeyboardDriver;
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;

import java.time.LocalTime;


public class JoyItKeyboardPiRunner implements Runner {

    private static final Object object = new Object();
    private final TVReader tvReader;
    private final Config config;
    private final JoyItKeyboardDriver keyboard;
    private final LocalTime now;
    private int channelCursor;

    public JoyItKeyboardPiRunner() {
        tvReader = new TVReader();
        config = new Config();
        keyboard = new JoyItKeyboardDriver();

        channelCursor = 0;
        now = LocalTime.now();
    }

    public void execute() throws InterruptedException {
        TVProgramBuilder builder = config.getTvProgramBuilder();


        synchronized (object) {
            int keyPressed = keyboard.getKeyPressed();
            if (keyPressed == config.getJoyItChannelUp()) {
                System.out.println("Channel UP pressed");
                InteractiveTVGuide tvGuide = new InteractiveTVGuide(config, builder.getTodayProgram(), now, channelCursor);
                channelCursor = tvGuide.channelUp();
                try {
                    tvReader.stop();
                    tvReader.read(tvGuide.getTimeCursor(), tvGuide);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (keyPressed == config.getJoyItChannelDown()) {
                System.out.println("Channel Down pressed");
                InteractiveTVGuide tvGuide = new InteractiveTVGuide(config, builder.getTodayProgram(), now, channelCursor);
                channelCursor = tvGuide.channelDown();
                try {
                    tvReader.stop();
                    tvReader.read(tvGuide.getTimeCursor(), tvGuide);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (keyPressed == config.getJoyItTimeUp()) {
                System.out.println("Time UP pressed");
                InteractiveTVGuide tvGuide = new InteractiveTVGuide(config, builder.getTodayProgram(), now, channelCursor);
                now = tvGuide.timeUp();
                try {
                    tvReader.stop();
                    tvReader.read(tvGuide.getTimeCursor(), tvGuide);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (keyPressed == config.getJoyItTimeDown()) {
                System.out.println("Time Down pressed");
                InteractiveTVGuide tvGuide = new InteractiveTVGuide(config, builder.getTodayProgram(), now, channelCursor);
                now = tvGuide.timeDown();
                try {
                    tvReader.stop();
                    tvReader.read(tvGuide.getTimeCursor(), tvGuide);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Keep program running until user aborts (CTRL-C)");
        while (true) {
            Thread.sleep(1000);
        }
    }


}
