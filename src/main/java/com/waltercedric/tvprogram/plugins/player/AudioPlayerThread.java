package com.waltercedric.tvprogram.plugins.player;

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

    public void play(AudioInputStream audioStream) {
        stopPlayer();

        player = new AudioPlayer();
        player.setAudio(audioStream);
        player.setPriority(10);
        playerThread = new Thread(this, "Audio player thread");
        playerThread.start();
    }

    public void stopPlayer() {
        if (player != null) {
            player.cancel();
            player = null;
            playerThread = null;
        }
    }
}
