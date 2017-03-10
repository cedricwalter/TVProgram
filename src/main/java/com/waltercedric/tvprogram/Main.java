package com.waltercedric.tvprogram;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.waltercedric.tvprogram.guide.TVGuide;
import com.waltercedric.tvprogram.guide.TVGuideFromTo;
import com.waltercedric.tvprogram.guide.TVGuideNow;
import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;

import java.time.LocalTime;

class Main {

    private static Object object = new Object();

    public static void main(String[] args) throws Exception {
        if ("pi".equals(args[0])) {
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
                            LocalTime now = LocalTime.now();
                            Config config = new Config();
                            TVProgramBuilder builder = config.getTvProgramBuilder();
                            TVGuide guideNow = new TVGuideNow(builder.getTodayProgram(), now);
                            try {
                                new TVReader().read(guideNow);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            System.out.println("keep program running until user aborts (CTRL-C)");
            while (true) {
                Thread.sleep(1000);
            }
        } else if ("now".equals(args[0])) {
            Config config = new Config();
            TVProgramBuilder builder = config.getTvProgramBuilder();
            LocalTime now = LocalTime.now();
            TVGuide guideNow = new TVGuideNow(builder.getTodayProgram(), now);
            new TVReader().read(guideNow);
        } else if ("program".equals(args[0])) {
            Config config = new Config();
            TVProgramBuilder builder = config.getTvProgramBuilder();
            TVGuide guideFromTo = new TVGuideFromTo(builder.getTodayProgram(), LocalTime.parse(args[1]), LocalTime.parse(args[2]));
            new TVReader().read(guideFromTo);
        }
    }

}