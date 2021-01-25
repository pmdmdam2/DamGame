package com.example.damgame.controllers;

import android.media.MediaPlayer;
import com.example.damgame.activities.GameActivity;
import com.example.damgame.views.Scene;

/**
 * Controlador de audio
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class AudioController {
    private GameActivity gameActivity;
    private MediaPlayer mediaPlay;
    private boolean isMediaExplosionStarted;
    public AudioController(GameActivity gameActivity){
        this.gameActivity = gameActivity;
    }
    public void startAudioPlay(Scene scene){
        this.mediaPlay = MediaPlayer.create(gameActivity,scene.getAudioPlay());
        this.mediaPlay.setLooping(true);
        this.mediaPlay.start();
    }
    public void stopAudioPlay(){
        this.mediaPlay.stop();
        this.mediaPlay = null;
    }
    public void startAudioExplosion(Scene scene){
        MediaPlayer mediaExplosion = MediaPlayer.create(gameActivity,scene.getAudioExplosion());
        mediaExplosion.start();
        this.isMediaExplosionStarted = true;
    }
    public void starAudioCrash(Scene scene){
        MediaPlayer mediaCrash = MediaPlayer.create(gameActivity, scene.getAudioCrash());
        mediaCrash.start();
    }
    public boolean isMediaExplosionStarted(){
        return this.isMediaExplosionStarted;
    }
}
