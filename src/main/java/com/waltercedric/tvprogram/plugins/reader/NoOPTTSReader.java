package com.waltercedric.tvprogram.plugins.reader;

public class NoOPTTSReader implements TTSReader {

    public void play(String sentenceToPlay) {
        System.out.println(sentenceToPlay);
    }

}
