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

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVReader;
import com.waltercedric.tvprogram.guide.InteractiveTVGuide;
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;

import java.time.LocalTime;


public class CustomPiRunner implements Runner {

    private static final Object object = new Object();
    private static GpioController gpio;
    private final TVReader tvReader;
    private final Config config;
    private LocalTime now;
    private int channelCursor;

    public CustomPiRunner() {
        gpio = GpioFactory.getInstance();
        tvReader = new TVReader();
        config = new Config();
        channelCursor = 0;
        now = LocalTime.now();
    }

    public void execute() throws InterruptedException {
        TVProgramBuilder builder = config.getTvProgramBuilder();

        System.out.println("Starting listener for channel up on pin " + config.getChannelUpPin());
        final GpioPinDigitalInput channelUp = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.getChannelUpPin()), PinPullResistance.PULL_DOWN);
        channelUp.setShutdownOptions(true);
        channelUp.addListener((GpioPinListenerDigital) event -> {
            if (event.getState().equals(PinState.HIGH)) {
                synchronized (object) {
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
            }
        });

        System.out.println("Starting listener for channel down on pin " + config.getChannelDownPin());
        final GpioPinDigitalInput channelDown = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.getChannelDownPin()), PinPullResistance.PULL_DOWN);
        channelDown.setShutdownOptions(true);
        channelDown.addListener((GpioPinListenerDigital) event -> {
            if (event.getState().equals(PinState.HIGH)) {
                synchronized (object) {
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
            }
        });

        System.out.println("Starting listener for time up on pin " + config.getTimeUpPin());
        final GpioPinDigitalInput timeUp = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.getTimeUpPin()), PinPullResistance.PULL_DOWN);
        timeUp.setShutdownOptions(true);
        timeUp.addListener((GpioPinListenerDigital) event -> {
            if (event.getState().equals(PinState.HIGH)) {
                synchronized (object) {
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
            }
        });

        System.out.println("Starting listener for time down on pin " + config.getTimeDownPin());
        final GpioPinDigitalInput timeDown = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.getTimeDownPin()), PinPullResistance.PULL_DOWN);
        timeDown.setShutdownOptions(true);
        timeDown.addListener((GpioPinListenerDigital) event -> {
            if (event.getState().equals(PinState.HIGH)) {
                synchronized (object) {
                    System.out.println("Time down pressed");
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
        });

        System.out.println("Now listening to PIN changes, keep program running until user aborts (CTRL-C)");
        while (true) {
            Thread.sleep(200);
        }
    }


}
