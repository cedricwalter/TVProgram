package com.waltercedric.tvprogram.plugins.player;

import javazoom.jl.decoder.JavaLayerException;
import marytts.util.data.audio.AudioPlayer;

import javax.sound.sampled.AudioInputStream;

public class AudioPlayerThread implements Runnable {
    private AudioPlayer player;
    private Thread playerThread = null;

    @Override
    public void run() {
        if (player != null) {
            player.run();
        }
    }

    public void play(AudioInputStream audioStream) throws JavaLayerException {
        stopPlayer();

        player = new AudioPlayer();
        player.setAudio(audioStream);
        player.setPriority(10);
        playerThread = new Thread(this, "Audio player thread");
        playerThread.start();
    }

    public void stopPlayer() throws JavaLayerException {
        if (player != null) {
            player.cancel();
            player = null;
            playerThread = null;
        }
    }
}
