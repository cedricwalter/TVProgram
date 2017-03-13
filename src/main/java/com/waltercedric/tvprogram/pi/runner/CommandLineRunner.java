package com.waltercedric.tvprogram.pi.runner;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVReader;
import com.waltercedric.tvprogram.guide.InteractiveTVGuide;
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;

import java.time.LocalTime;
import java.util.Scanner;


public class CommandLineRunner implements Runner {

    private final Config config;
    private final TVReader tvReader;
    private int channelCursor;
    private LocalTime now;

    public CommandLineRunner() {

        config = new Config();
        tvReader = new TVReader();
        now = LocalTime.now();
        channelCursor = 0;
    }

    public void execute() throws InterruptedException {
        TVProgramBuilder builder = config.getTvProgramBuilder();

        System.out.println("Waiting for key +/- for time    d/f for channel");
        while (true) {
            Scanner keyboard = new Scanner(System.in);
            String input = keyboard.nextLine();
            System.out.println(input);

            if (input.equals("d")) {
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
            if (input.equals("f")) {
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

            if (input.equals("+")) {
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

            if (input.equals("-")) {
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

            Thread.sleep(200);
        }
    }


}
