package com.waltercedric.tvprogram.plugins.reader;

import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.TVProgram;
import com.waltercedric.tvprogram.guide.TVGuide;
import com.waltercedric.tvprogram.plugins.mapping.TimeToTextConverter;
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
    private final MaryInterface maryTTS;
    private static Object object = new Object();
    private final TimeToTextConverter timeToTextConverter;


    public TVReader() {
        try {
            maryTTS = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            throw new RuntimeException(e);
        }
        maryTTS.setVoice(config.getVoice());
        timeToTextConverter = config.getTimeToTextConverter();
    }

    public void read(TVGuide guide) throws Exception {
        List<TVProgram> programs = guide.getProgram();

        play(guide.getIntroduction());
        for (TVProgram tvProgram : programs) {
            play(tvProgram, guide.getForEachProgram());
        }
    }

    private void play(TVProgram tvProgram, String sentence) throws Exception {
        Template template = new Template("templateName", new StringReader(sentence), new Configuration());

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("channel", tvProgram.getChannel());
        parameters.put("title", tvProgram.getTitle());
        parameters.put("description", tvProgram.getDescription());
        parameters.put("category", tvProgram.getCategory());
        parameters.put("start", timeToTextConverter.convertTimeToText(tvProgram.getStartTime().toString()));
        parameters.put("end", timeToTextConverter.convertTimeToText(tvProgram.getEndTime().toString()));
        parameters.put("duration", tvProgram.getDuration().toString());

        StringWriter output = new StringWriter();
        template.process(parameters, output);

        String sentenceToPlay = output.toString();

        play(sentenceToPlay);
    }


    private void play(String sentenceToPlay) throws MaryConfigurationException, SynthesisException {
        synchronized (object) {
            AudioPlayer player = new AudioPlayer();
            player.setAudio(maryTTS.generateAudio(sentenceToPlay));
            player.setPriority(10);
            player.run();
        }
    }

}
