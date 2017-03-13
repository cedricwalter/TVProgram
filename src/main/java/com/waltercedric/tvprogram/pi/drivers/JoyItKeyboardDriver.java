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
package com.waltercedric.tvprogram.pi.drivers;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class JoyItKeyboardDriver {

    private final List<PropertyChangeListener> listener = new ArrayList<>();

    private static final String KEY = "key";
    private int keyPressed;

    private static final int keypad[][] = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
    };

    private final GpioController gpio = GpioFactory.getInstance();

    // 1,2,3,4 = columns = input
    private final GpioPinDigitalInput thePin1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21, PinPullResistance.PULL_UP);
    private final GpioPinDigitalInput thePin2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, PinPullResistance.PULL_UP);
    private final GpioPinDigitalInput thePin3 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_17, PinPullResistance.PULL_UP);
    private final GpioPinDigitalInput thePin4 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP);

    // 5,6,7,8 = lines = output
    private final GpioPinDigitalOutput outputPins[] = {
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25)};

    private GpioPinDigitalInput theInput;

    private int lineSelected;
    private int columnSelected;

    public JoyItKeyboardDriver() {
        thePin1.addListener((GpioPinListenerDigital) aEvent -> {
            if (aEvent.getState() == PinState.LOW) {
                theInput = thePin1;
                lineSelected = 1;
                findOutput();
            }
        });
        thePin2.addListener((GpioPinListenerDigital) aEvent -> {
            if (aEvent.getState() == PinState.LOW) {
                theInput = thePin2;
                lineSelected = 2;
                findOutput();
            }
        });
        thePin3.addListener((GpioPinListenerDigital) aEvent -> {
            if (aEvent.getState() == PinState.LOW) {
                theInput = thePin3;
                lineSelected = 3;
                findOutput();
            }
        });
        thePin4.addListener((GpioPinListenerDigital) aEvent -> {
            if (aEvent.getState() == PinState.LOW) {
                theInput = thePin4;
                lineSelected = 4;
                findOutput();
            }
        });
    }

    /**
     * Find output.
     * <p>
     * Sets output lines to high and then to low one by one. Then the input line
     * is tested. If its state is low, we have the right output line and
     * therefore a mapping to a key on the keypad.
     */
    private void findOutput() {
        // now test the inputs by setting the outputs from high to low
        // one by one
        for (int outputPinCounter = 0; outputPinCounter < outputPins.length; outputPinCounter++) {
            for (GpioPinDigitalOutput outputPin : outputPins) {
                outputPin.high();
            }

            outputPins[outputPinCounter].low();

            // input found?
            if (theInput.isLow()) {
                columnSelected = outputPinCounter;
                checkPins();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    //do nothing
                }
                break;
            }
        }

        for (final GpioPinDigitalOutput myTheOutput : outputPins) {
            myTheOutput.low();
        }
    }

    /**
     * Check pins.
     * Determines the pressed key based on the activated GPIO pins.
     */
    private synchronized void checkPins() {
        int oldValue = this.keyPressed;
        int newValue = this.keyPressed = keypad[lineSelected - 1][columnSelected];

        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, KEY,
                    oldValue, newValue));
        }
        System.out.println(keypad[lineSelected - 1][columnSelected]);
    }

    public int getKeyPressed() {
        return keyPressed;
    }
}