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
package com.waltercedric.tvprogram.plugins.reader;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.polly.AmazonPollyAsync;
import com.amazonaws.services.polly.AmazonPollyAsyncClient;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.plugins.player.Player;

import java.io.InputStream;
import java.util.concurrent.Future;


public class PollyTTSReader implements TTSReader {

    private static final Config config = new Config();
    private static final Object object = new Object();
    private final AmazonPollyAsync polly;
    private final Player myplayer;

    public PollyTTSReader() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(config.getIam_access(), config.getIam_secret());

        polly = AmazonPollyAsyncClient.asyncBuilder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(config.getAws_region()).build();

        myplayer = config.getPlayer();
    }

    public void play(String sentenceToPlay) {
        synchronized (object) {
            SynthesizeSpeechRequest tssRequest = newRequest();
            tssRequest.setText(sentenceToPlay);

            Future<SynthesizeSpeechResult> synthesizeSpeechResultFuture = polly.synthesizeSpeechAsync(tssRequest);
            try {
                InputStream audioStream = synthesizeSpeechResultFuture.get().getAudioStream();
                myplayer.play(audioStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        myplayer.stopPlayer();
    }

    private SynthesizeSpeechRequest newRequest() {
        SynthesizeSpeechRequest tssRequest = new SynthesizeSpeechRequest();
        tssRequest.setVoiceId(config.getVoiceid());
        tssRequest.setOutputFormat(OutputFormat.Mp3);

        return tssRequest;
    }

}
