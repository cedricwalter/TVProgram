package com.waltercedric.tvprogram.plugins.reader;

import com.waltercedric.tvprogram.Config;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;


public class MaryTTSReader implements TVReader {

    private static final Config config = new Config();
    private final MaryInterface maryTTS;
    private static Object object = new Object();

    public MaryTTSReader() {
        try {
            maryTTS = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            throw new RuntimeException(e);
        }
        maryTTS.setVoice(config.getVoice());

    }

    public void play(String sentenceToPlay) {
        synchronized (object) {
            AudioPlayer player = new AudioPlayer();
            try {
                player.setAudio(maryTTS.generateAudio(sentenceToPlay));
            } catch (SynthesisException e) {
                throw new RuntimeException(e);
            }
            player.setPriority(10);
            player.run();
        }
    }

}
