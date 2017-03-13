package com.waltercedric.tvprogram.plugins.reader;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.plugins.player.AudioPlayerThread;
import javazoom.jl.decoder.JavaLayerException;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;

import javax.sound.sampled.AudioInputStream;


public class MaryTTSReader implements TTSReader {

    private static final Config config = new Config();
    private final MaryInterface maryTTS;
    private static Object object = new Object();
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
            try {
                myplayer.stopPlayer();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }

}
