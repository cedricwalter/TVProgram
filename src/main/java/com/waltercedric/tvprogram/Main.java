package com.waltercedric.tvprogram;

import com.waltercedric.tvprogram.plugins.sources.TVProgramBuilder;
import com.waltercedric.tvprogram.plugins.sources.Webnext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

class Main {

    private static final Config config = new Config();

    public static void main(String[] args) throws Exception {
        TVProgramBuilder builder =  new Webnext();

        TVGuide guide = new TVGuide(builder.getTodayProgram());
        LocalTime now = LocalTime.now();
        List<TVProgram> program = guide.getProgram(now, 10, 5);

        for (TVProgram tvProgram : program) {
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
