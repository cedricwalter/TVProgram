package com.waltercedric.tvprogram;

import com.waltercedric.tvprogram.guide.TVGuide;
import com.waltercedric.tvprogram.plugins.mapping.TimeToTextConverter;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

public class TVReader {

    private static final Config config = new Config();
    private final TimeToTextConverter timeToTextConverter;
    private final com.waltercedric.tvprogram.plugins.reader.TVReader tvReader;


    public TVReader() {
        this.timeToTextConverter = config.getTimeToTextConverter();
        this.tvReader = config.getTVReader();
    }

    public void read(TVGuide guide) throws Exception {
        List<TVProgram> programs = guide.getProgram();

        tvReader.play(guide.getIntroduction());
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

        tvReader.play(sentenceToPlay);
    }


}
