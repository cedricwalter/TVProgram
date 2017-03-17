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
package com.waltercedric.tvprogram;

import com.waltercedric.tvprogram.guide.TVGuide;
import com.waltercedric.tvprogram.plugins.mapping.TimeToTextConverter;
import com.waltercedric.tvprogram.plugins.reader.TTSReader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

public class TVReader {

    private static final Config config = new Config();
    private final TimeToTextConverter timeToTextConverter;
    private final TTSReader TTSReader;

    public TVReader() {
        this.timeToTextConverter = config.getTimeToTextConverter();
        this.TTSReader = config.getTTSReader();
    }

    public void read(LocalTime time, TVGuide guide) throws Exception {
        List<TVProgram> programs = guide.getProgram();

        TTSReader.play(guide.getIntroduction());
        for (TVProgram tvProgram : programs) {
            play(time, tvProgram, guide.getForEachProgram());
        }
    }

    private void play(LocalTime time, TVProgram tvProgram, String sentence) throws Exception {
        Template template = new Template("templateName", new StringReader(sentence), new Configuration());

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("channel", tvProgram.getChannel());
        parameters.put("title", tvProgram.getTitle());
        parameters.put("description", tvProgram.getDescription());
        parameters.put("category", tvProgram.getCategory());
        parameters.put("start", timeToTextConverter.convertTimeToText(tvProgram.getStartTime().toString()));
        parameters.put("end", timeToTextConverter.convertTimeToText(tvProgram.getEndTime().toString()));
        parameters.put("duration", tvProgram.getDuration().toString());
        parameters.put("left", String.valueOf(tvProgram.getRestTime()));
        parameters.put("time", time.getHour() + ":" + time.getMinute());

        StringWriter output = new StringWriter();
        template.process(parameters, output);

        String sentenceToPlay = output.toString();

        TTSReader.play(sentenceToPlay);
    }

    public void stop() {
        TTSReader.stop();
    }

}
