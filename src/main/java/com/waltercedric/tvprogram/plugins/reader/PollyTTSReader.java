package com.waltercedric.tvprogram.plugins.reader;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.waltercedric.tvprogram.Config;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;


public class PollyTTSReader implements TTSReader {

    private static final Config config = new Config();
    private static Object object = new Object();
    private volatile BasicAWSCredentials awsCreds;
    private final AmazonPollyClient polly;
    private AdvancedPlayer player;

    public PollyTTSReader() {
        awsCreds = new BasicAWSCredentials(config.getIam_access(), config.getIam_secret());

        polly = (AmazonPollyClient) AmazonPollyClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(config.getAws_region())
                .build();
    }

    public void play(String sentenceToPlay) {
        synchronized (object) {
            SynthesizeSpeechRequest tssRequest = newRequest();
            tssRequest.setText(sentenceToPlay);

            playSpeechResult(polly.synthesizeSpeech(tssRequest));
        }
    }

    @Override
    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    private void playSpeechResult(SynthesizeSpeechResult speechResult) {
        try {
            player = new AdvancedPlayer(speechResult.getAudioStream(),
                    javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());
//            player.setPlayBackListener(new PlaybackListener() {
//                @Override
//                public void playbackStarted(PlaybackEvent evt) {
//                    System.out.println("Polly Playback started");
//                }
//
//                @Override
//                public void playbackFinished(PlaybackEvent evt) {
//                    System.out.println("Polly Playback finished");
//                }
//            });

            player.play();
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    private SynthesizeSpeechRequest newRequest() {
        SynthesizeSpeechRequest tssRequest = new SynthesizeSpeechRequest();
        tssRequest.setVoiceId(config.getVoiceid());
        tssRequest.setOutputFormat(OutputFormat.Mp3);

        return tssRequest;
    }

}
