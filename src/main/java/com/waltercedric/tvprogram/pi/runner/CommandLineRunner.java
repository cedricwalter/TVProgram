/**
 * Copyright (c) 2017-2017 by Cédric Walter - www.cedricwalter.com
 * <p>
 * TVProgram is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * TVProgram is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
