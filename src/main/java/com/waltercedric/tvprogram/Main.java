package com.waltercedric.tvprogram;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.waltercedric.tvprogram.guide.InteractiveTVGuide;
import com.waltercedric.tvprogram.guide.TVGuide;
import com.waltercedric.tvprogram.guide.TVGuideFromTo;
import com.waltercedric.tvprogram.guide.TVGuideNow;
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;

import java.time.LocalTime;

class Main {

    private static Object object = new Object();

    public static void main(String[] args) throws Exception {
        if ("pi".equals(args[0])) {
            executeForPI();
        } else if ("now".equals(args[0])) {
            executeNowOnTV();
        } else if ("program".equals(args[0])) {
            executeTVProgram(args);
        } else if ("interactive".equals(args[0])) {
            executeInteractive(args);
        }
    }

    private static void executeInteractive(String[] args) throws InterruptedException {
        final GpioController gpio = GpioFactory.getInstance();

        Config config = new Config();
        TVProgramBuilder builder = config.getTvProgramBuilder();
        LocalTime now = LocalTime.now();

        InteractiveTVGuide tvGuide = new InteractiveTVGuide(config, builder.getTodayProgram(), now);

        TVReader tvReader = new TVReader();

        final GpioPinDigitalInput channelUp = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(config.getChannelUpPin()), PinPullResistance.PULL_DOWN);
        channelUp.setShutdownOptions(true);
        channelUp.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState().equals(PinState.HIGH)) {
                    synchronized (object) {
                        tvGuide.channelUp();
                        try {
                            tvReader.stop();
                            tvReader.read(tvGuide);
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
                        tvGuide.channelDown();
                        try {
                            tvReader.stop();
                            tvReader.read(tvGuide);
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
                        tvGuide.timeUp();
                        try {
                            tvReader.stop();
                            tvReader.read(tvGuide);
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
                        tvGuide.timeDown();
                        try {
                            tvReader.stop();
                            tvReader.read(tvGuide);
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


    /**
     * read GPIO pin, need to run as root to be able to read pin
     *
     * @throws InterruptedException
     */
    private static void executeForPI() throws InterruptedException {
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput digitalButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
        digitalButton.setShutdownOptions(true);
        digitalButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println(" --> Digital: " + event.getPin() + " = " + event.getState());

                if (event.getState().equals(PinState.HIGH)) {
                    synchronized (object) {
                        try {
                            executeNowOnTV();
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

    /**
     * You may want to call this after user press on a button
     *
     * @throws Exception
     */
    private static void executeNowOnTV() throws Exception {
        Config config = new Config();
        TVProgramBuilder builder = config.getTvProgramBuilder();
        LocalTime now = LocalTime.now();
        TVGuide guideNow = new TVGuideNow(config, builder.getTodayProgram(), now);
        new TVReader().read(guideNow);
    }

    /**
     * You may want to call this in a crontab
     *
     * @param args
     * @throws Exception
     */
    private static void executeTVProgram(String[] args) throws Exception {
        Config config = new Config();
        TVProgramBuilder builder = config.getTvProgramBuilder();
        TVGuide guideFromTo = new TVGuideFromTo(config, builder.getTodayProgram(), LocalTime.parse(args[1]), LocalTime.parse(args[2]));
        new TVReader().read(guideFromTo);
    }

}