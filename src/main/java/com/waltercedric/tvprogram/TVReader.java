package com.waltercedric.tvprogram;

import com.waltercedric.tvprogram.guide.TVGuide;
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

    public void read(TVGuide guide) throws Exception {
        List<TVProgram> programs = guide.getProgram();

        play(guide.getIntroduction());
        for (TVProgram tvProgram : programs) {
            play(tvProgram, guide.getForEachProgram());
        }
    }

    private synchronized static void play(TVProgram tvProgram, String sentence) throws Exception {
        Template template = new Template("templateName", new StringReader(sentence), new Configuration());

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("channel", tvProgram.getChannel());
        parameters.put("title", tvProgram.getTitle());
        parameters.put("description", tvProgram.getDescription());
        parameters.put("category", tvProgram.getCategory());
        parameters.put("start", tvProgram.getStartTime().toString());
        parameters.put("end", tvProgram.getEndTime().toString());
        parameters.put("duration", tvProgram.getDuration().toString());

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
