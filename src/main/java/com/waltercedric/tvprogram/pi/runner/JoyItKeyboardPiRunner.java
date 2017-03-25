/**
 * Copyright (c) 2017-2017 by Cédric Walter - www.cedricwalter.com
 *
 * TVProgram is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TVProgram is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    private LocalTime now;
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
        try {
            while (true) {
                Thread.sleep(1000);
            }
        } finally {
            keyboard.shutdown();
        }
    }


}
