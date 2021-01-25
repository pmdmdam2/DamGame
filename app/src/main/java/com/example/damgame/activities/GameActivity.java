package com.example.damgame.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.damgame.R;
import com.example.damgame.controllers.AudioController;
import com.example.damgame.fragments.QuestionDialogFragment;
import com.example.damgame.interfaces.InterfaceDialogs;
import com.example.damgame.model.GameConfig;
import com.example.damgame.model.Play;
import com.example.damgame.model.Question;
import com.example.damgame.utils.GameUtil;
import com.example.damgame.views.GameView;
import com.example.damgame.views.Scene;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Actividad principal
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class GameActivity extends AppCompatActivity implements InterfaceDialogs {
    private final int SETTINGS_ACTION =1;
    private Play gameMove;
    private int sceneCode;
    private GameView gameView;
    private GameConfig config;
    private Scene scene;
    private AudioController audioController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTema();
        setContentView(R.layout.activity_main);
        this.config = new GameConfig();

        Button btIniciar = findViewById(R.id.btIniciar);
        btIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
    }

    private void startGame(){
        this.sceneCode = Integer.valueOf(getDefaultSharedPreferences(this).
                getString("ambient_setting","100"));
        this.gameMove = Play.createGameMove(this,this.sceneCode, this.config);
        this.scene = this.gameMove.getScene();
        this.gameView = new GameView(this);
        this.audioController = this.gameView.getAudioController();

        this.audioController.startAudioPlay(this.scene);
        hideSystemUI();
        setContentView(this.gameView);
    }

    private void showQuestionDialog(){
        //código para probar el cuadro de diálogo
        Button btPregunta = findViewById(R.id.btIniciar);

        MediaPlayer mediaPlayerJuego = MediaPlayer.create(this, R.raw.my_street);
        mediaPlayerJuego.setLooping(true);
        mediaPlayerJuego.start();

        btPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showQuestionDialog();
                startGame();
            }
        });
        //aquí habrá que obtener aleatoriamente una pregunta
        //del repositorio
        CharSequence[] respuestas = new CharSequence[3];
        String enunciado = "Selecciona cuál de los siguientes planetas no está en la vía láctea";
        respuestas[0]="Marte";
        respuestas[1]="Nébula";
        respuestas[2]="Mercurio";
        String respuestaCorrecta = respuestas[1].toString();
        Question question = new Question(enunciado, GameUtil.PREGUNTA_COMPLEJIDAD_ALTA,
                GameUtil.PREGUNTA_SIMPLE,respuestas,respuestaCorrecta,20);

        QuestionDialogFragment qdf = new QuestionDialogFragment(question, GameActivity.this);
        qdf.setCancelable(false);
        qdf.show(getSupportFragmentManager(),null);
    }

    /**
     * Establece el tema seleccionado en las preferencias
     */
    private void setTema(){
        this.sceneCode = Integer.valueOf(getDefaultSharedPreferences(this).
                getString("theme_setting","100"));
        switch(this.sceneCode){
            case GameUtil.TEMA_DESIERTO:
                setTheme(R.style.Desert_DamGame);
                this.scene = this.getJugada().getScene();
                break;
            default:
                setTheme(R.style.Desert_DamGame);
                break;
        }

    }
    private void hideSystemUI() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            //A partir de kitkat
            this.gameView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            //cuando se presiona volumen, por ejemplo, se cambia la visibilidad, hay que volver
            //a ocultar
            this.gameView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    hideSystemUI();
                }
            });
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            // Pre-Jelly Bean, we must manually hide the action bar
            // and use the old window flags API.
            getActionBar().hide();
        }
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public GameView getGameView(){
        return this.gameView;
    }
    public Play getJugada(){
        return this.gameMove;
    }
    public GameConfig getGameConfig(){
        return this.config;
    }
    public AudioController getAudioController(){
        return this.audioController;
    }
    @Override
    public void setRespuesta(String respuesta) {
        Toast.makeText(this,respuesta,Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.imSettings:
                Intent preferences = new Intent(GameActivity.this,SettingsActivity.class);
                startActivityForResult(preferences, SETTINGS_ACTION);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== SETTINGS_ACTION){
            if(resultCode== Activity.RESULT_OK){

            }
        }
    }
}