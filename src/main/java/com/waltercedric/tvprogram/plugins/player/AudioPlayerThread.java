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
