package com.waltercedric.tvprogram;

import freemarker.template.Configuration;
import freemarker.template.Template;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;


public class TVReader {

    private static final Config config = new Config();

    public void read(List<TVProgram> programs) throws Exception {
        for (TVProgram tvProgram : programs) {
            play(tvProgram);
        }
    }

    private synchronized static void play(TVProgram tvProgram) throws Exception {
        MaryInterface marytts = new LocalMaryInterface();
        marytts.setVoice(config.getVoice());
        AudioPlayer player = new AudioPlayer();

        Template template = new Template("templateName", new StringReader(config.getSentenceNow()), new Configuration());

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("channel", tvProgram.getChannel());
        parameters.put("title", tvProgram.getTitle());
        parameters.put("description", tvProgram.getDescription());
        parameters.put("category", tvProgram.getCategory());

        StringWriter output = new StringWriter();
        template.process(parameters, output);

        player.setAudio(marytts.generateAudio(output.toString()));
        player.run();
    }

}
