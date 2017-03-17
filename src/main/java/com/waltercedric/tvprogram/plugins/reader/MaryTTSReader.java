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
package com.waltercedric.tvprogram.plugins.reader;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.plugins.player.AudioPlayerThread;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;

import javax.sound.sampled.AudioInputStream;


public class MaryTTSReader implements TTSReader {

    private static final Config config = new Config();
    private final MaryInterface maryTTS;
    private static final Object object = new Object();
    private final AudioPlayerThread myplayer;


    public MaryTTSReader() {
        try {
            maryTTS = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            throw new RuntimeException(e);
        }
        maryTTS.setVoice(config.getVoice());

        myplayer = new AudioPlayerThread();
    }

    public void play(String sentenceToPlay) {
        synchronized (object) {
            try {
                if (!"".equals(sentenceToPlay)) {
                    AudioInputStream audioInputStream = maryTTS.generateAudio(sentenceToPlay);
                    myplayer.play(audioInputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        if (myplayer != null) {
            myplayer.stopPlayer();
        }
    }

}
