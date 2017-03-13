package com.waltercedric.tvprogram.plugins.player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.InputStream;

public class JavaZoomPlayerThread implements Runnable {
    private AdvancedPlayer player = null;
    private Thread playerThread = null;

    @Override
    public void run() {
        if (player != null) {
            try {
                player.play();
            } catch (JavaLayerException ex) {
                System.err.println("Problem playing audio: " + ex);
            }
        }
    }

    public void play(InputStream audioStream) throws JavaLayerException {
        stopPlayer();

        player = new AdvancedPlayer(audioStream, FactoryRegistry.systemRegistry().createAudioDevice());
        playerThread = new Thread(this, "Audio player thread");
        playerThread.start();
    }

    public void stopPlayer() {
        if (player != null) {
            player.close();
            player = null;
            playerThread = null;
        }
    }
}
