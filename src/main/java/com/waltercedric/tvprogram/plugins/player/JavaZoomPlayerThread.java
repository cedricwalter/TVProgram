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
package com.waltercedric.tvprogram.plugins.player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.InputStream;

public class JavaZoomPlayerThread implements Player, Runnable {
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

    public void play(InputStream audioStream) {
        stopPlayer();
        try {
            player = new AdvancedPlayer(audioStream, FactoryRegistry.systemRegistry().createAudioDevice());
            playerThread = new Thread(this, "Audio player thread");
            playerThread.setPriority(10);
            playerThread.start();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public void stopPlayer() {
        if (player != null) {
            player.close();
            player = null;
            playerThread = null;
        }
    }
}
