package com.waltercedric.tvprogram.pi;


import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVReader;
import com.waltercedric.tvprogram.guide.InteractiveTVGuide;
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;

import java.time.LocalTime;

public class InteractivePiTVGuide {

    private static Object object = new Object();
    private static GpioController gpio;
    private final TVReader tvReader;
    private final Config config;

    public InteractivePiTVGuide() {
        gpio = GpioFactory.getInstance();
        tvReader = new TVReader();
        config = new Config();
    }

    public void execute() throws InterruptedException {
        TVProgramBuilder builder = config.getTvProgramBuilder();
        LocalTime now = LocalTime.now();

        final GpioPinDigitalInput channelUp = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.getChannelUpPin()), PinPullResistance.PULL_DOWN);
        channelUp.setShutdownOptions(true);
        channelUp.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState().equals(PinState.HIGH)) {
                    synchronized (object) {
                        InteractiveTVGuide tvGuide = new InteractiveTVGuide(config, builder.getTodayProgram(), now);
                        tvGuide.channelUp();
                        try {
                            tvReader.stop();
                            tvReader.read(tvGuide.getTimeCursor(), tvGuide);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        final GpioPinDigitalInput channelDown = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.getChannelDownPin()), PinPullResistance.PULL_DOWN);
        channelDown.setShutdownOptions(true);
        channelDown.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState().equals(PinState.HIGH)) {
                    synchronized (object) {
                        InteractiveTVGuide tvGuide = new InteractiveTVGuide(config, builder.getTodayProgram(), now);
                        tvGuide.channelDown();
                        try {
                            tvReader.stop();
                            tvReader.read(tvGuide.getTimeCursor(), tvGuide);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        final GpioPinDigitalInput timeUp = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.getTimeUpPin()), PinPullResistance.PULL_DOWN);
        timeUp.setShutdownOptions(true);
        timeUp.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState().equals(PinState.HIGH)) {
                    synchronized (object) {
                        InteractiveTVGuide tvGuide = new InteractiveTVGuide(config, builder.getTodayProgram(), now);
                        tvGuide.timeUp();
                        try {
                            tvReader.stop();
                            tvReader.read(tvGuide.getTimeCursor(), tvGuide);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        final GpioPinDigitalInput timeDown = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.getTimeDownPin()), PinPullResistance.PULL_DOWN);
        timeDown.setShutdownOptions(true);
        timeDown.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState().equals(PinState.HIGH)) {
                    synchronized (object) {
                        InteractiveTVGuide tvGuide = new InteractiveTVGuide(config, builder.getTodayProgram(), now);
                        tvGuide.timeDown();
                        try {
                            tvReader.stop();
                            tvReader.read(tvGuide.getTimeCursor(), tvGuide);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        System.out.println("Keep program running until user aborts (CTRL-C)");
        while (true) {
            Thread.sleep(1000);
        }
    }
}
