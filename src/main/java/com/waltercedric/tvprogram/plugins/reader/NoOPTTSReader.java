package com.waltercedric.tvprogram.plugins.reader;

public class NoOPTTSReader implements TVReader {

    public void play(String sentenceToPlay) {
        System.out.println(sentenceToPlay);
    }

}
