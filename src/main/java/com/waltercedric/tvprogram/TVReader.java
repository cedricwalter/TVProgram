package com.waltercedric.tvprogram;

import freemarker.template.Configuration;
import freemarker.template.Template;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;


public class TVReader {

    private static final Config config = new Config();

    public void read(List<TVProgram> programs) throws Exception {
        play(config.getSentenceNow_introduction());
        for (TVProgram tvProgram : programs) {
            play((TVProgram) tvProgram, config.getSentenceNow_each());
        }
    }

    private synchronized static void play(TVProgram tvProgram, String sentence) throws Exception {
        Template template = new Template("templateName", new StringReader(sentence), new Configuration());

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("channel", tvProgram.getChannel());
        parameters.put("title", tvProgram.getTitle());
        parameters.put("description", tvProgram.getDescription());
        parameters.put("category", tvProgram.getCategory());

        StringWriter output = new StringWriter();
        template.process(parameters, output);

        String sentenceToPlay = output.toString();

        play(sentenceToPlay);
    }

    private static void play(String sentenceToPlay) throws MaryConfigurationException, SynthesisException {
        MaryInterface marytts = new LocalMaryInterface();
        marytts.setVoice(config.getVoice());
        AudioPlayer player = new AudioPlayer();
        player.setAudio(marytts.generateAudio(sentenceToPlay));
        player.run();
    }

}
