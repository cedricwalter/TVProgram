package com.waltercedric.tvprogram.plugins.reader;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.polly.AmazonPollyAsync;
import com.amazonaws.services.polly.AmazonPollyAsyncClient;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.waltercedric.tvprogram.Config;
import com.waltercedric.tvprogram.plugins.player.JavaZoomPlayerThread;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.InputStream;
import java.util.concurrent.Future;


public class PollyTTSReader implements TTSReader {

    private static final Config config = new Config();
    private static Object object = new Object();
    private volatile BasicAWSCredentials awsCreds;
    private final AmazonPollyAsync polly;
    private AdvancedPlayer player;
    private InputStream audioStream;
    private final JavaZoomPlayerThread myplayer;

    public PollyTTSReader() {
        awsCreds = new BasicAWSCredentials(config.getIam_access(), config.getIam_secret());

        polly = AmazonPollyAsyncClient.asyncBuilder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(config.getAws_region()).build();

        myplayer = new JavaZoomPlayerThread();
    }

    public void play(String sentenceToPlay) {
        synchronized (object) {
            SynthesizeSpeechRequest tssRequest = newRequest();
            tssRequest.setText(sentenceToPlay);

            Future<SynthesizeSpeechResult> synthesizeSpeechResultFuture = polly.synthesizeSpeechAsync(tssRequest);
            try {
                audioStream = synthesizeSpeechResultFuture.get().getAudioStream();
                myplayer.play(audioStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        try {
            myplayer.stopPlayer();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    private SynthesizeSpeechRequest newRequest() {
        SynthesizeSpeechRequest tssRequest = new SynthesizeSpeechRequest();
        tssRequest.setVoiceId(config.getVoiceid());
        tssRequest.setOutputFormat(OutputFormat.Mp3);

        return tssRequest;
    }

}
