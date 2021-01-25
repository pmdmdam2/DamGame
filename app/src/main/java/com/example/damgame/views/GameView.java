package com.example.damgame.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import com.example.damgame.activities.GameActivity;
import com.example.damgame.controllers.AudioController;
import com.example.damgame.controllers.TouchController;
import com.example.damgame.model.GameConfig;
import com.example.damgame.model.Play;
import com.example.damgame.model.Question;
import com.example.damgame.model.Touch;
import com.example.damgame.threads.GameLoop;
import com.example.damgame.utils.GameUtil;

/**
 * Vista principal del juego, gestiona todos los personajes, objetos y escenas.
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private GameActivity gameActivity;
    private SurfaceHolder surfaceHolder;
    private GameLoop gameLoop;
    private Play play;
    private GameConfig gameConfig;
    //holder para el canvas
    private SurfaceHolder holder;
    //dimensiones de la pantalla
    private int screenHeight;
    private int screenWidth;
    //Objeto gráfico principal
    private BouncyView bouncyView;
    //Explosion
    private ExplosionView explosionView;
    //Imagen de escena
    private Scene scene;
    //imágenes para preguntas
    private QuestionView goEasyQuestion;
    private QuestionView goDificultQuestion;
    private Bitmap imagenes[];
    //Controlador de toque en la pantalla
    private TouchController touchController;
    //Controlador de audio
    private AudioController audioController;

    public GameView(GameActivity gameActivity) {
        super(gameActivity);
        this.gameActivity = gameActivity;
        this.touchController = new TouchController(this.gameActivity);
        this.audioController = new AudioController(this.gameActivity);
        this.gameConfig = gameActivity.getGameConfig();
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.play = gameActivity.getJugada();
        this.scene = this.play.getScene();
        //se obtiene el tamaño de la pantalla
        this.play.getScene().getScreenSize(this.gameActivity);
        this.screenWidth = this.play.getScene().getScreenWidth();
        this.screenHeight = this.play.getScene().getScreenHeight();
        //se carga el OGP y se calcula su posición inicial
        this.bouncyView = new BouncyView(this);
        //explosion, cuando el flappy choca o cae al suelo
        this.explosionView = new ExplosionView(this);
        //se carga el efecto animado de la captura de la pregunta

        //ambiente
        this.sceneLoad();
        //preguntas
        //this.questionLoad();
        this.setOnTouchListener(this.touchController);
    }
    public void render(Canvas canvas){
        Scene scene = this.play.getScene();
        if(canvas!=null) {
            //pinceles
            Paint myPaint = new Paint();
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setColor(Color.WHITE);

            Paint myPaint2 = new Paint();
            myPaint2.setStyle(Paint.Style.FILL);
            myPaint2.setTextSize(50);

            //dibujamos el fondo
            canvas.drawBitmap(imagenes[this.scene.getCurrentImgIndex()],this.scene.getxCurrentImg(),0,null);
            canvas.drawBitmap(imagenes[this.scene.getNextImgIndex()],this.scene.getxNextImg(),0,null);

            //Si ha ocurrido un toque en la pantalla "Touch", dibujar un círculo
            if(this.touchController.isTouched()){
                synchronized (this) {
                    for (Touch t : this.touchController.getTouches()) {
                        canvas.drawCircle(t.getX(), t.getY(), 100, myPaint);
                        //canvas.drawText(t.index + "", t.x, t.y, myPaint2);
                    }
                }
            }

            if(!this.play.isFinished()) {
                //dibuja el OGP (posición inicial a 1/5 de ancho y la mitad de alto)
                /*canvas.drawBitmap(this.bouncyView.getBouncyBitmap(), this.bouncyView.getCoordenadaX(),
                        this.bouncyView.getCoordenadaY(), null);*/

                //si flappy ha llegado al suelo
                if(this.bouncyView.isLanded()) {
                    this.explosionView.draw(canvas, myPaint);
                    if(!this.audioController.isMediaExplosionStarted())
                        this.audioController.startAudioExplosion(this.getScene());
                }
                else {
                    this.bouncyView.draw(canvas, myPaint);
                }
            }

            /*dibuja las preguntas
            for(QuestionView questionView: this.play.getQuestionViews())
                questionView.draw(canvas,myPaint);*/

            //aquí se dibujarán la animación de captura de una pregunta

            //aquí se escribirán los puntos conseguidos

            //aquí comprobaremos si se ha vencido para mostrar un mensaje en pantalla

            //aquí comprobaremos si ha perdio para mostrar el texto correspondiente
        }
    }
    /**
     * Este método actualiza el estado del juego. Contiene la lógica del videojuego
     * generando los nuevos estados y dejando listo el sistema para un repintado.
     */
    public void updateState() {
        if(this.play.isFinished()) return;

        this.updateSceneBackground();

        /*Enemigos*/
        if(this.gameConfig.getFramesToNewQuestion()==0){
            this.createNewQuestion();
            //nuevo ciclo de enemigos
            this.gameConfig.setFramesToNewQuestion(GameLoop.MAX_FPS*60/this.gameConfig.getMinutesToQuestion());
        }
        this.gameConfig.setFramesToNewQuestion(this.gameConfig.getFramesToNewQuestion()-1);

        //las preguntas aparecen
        for(QuestionView goQuestion: this.play.getQuestionViews())
            goQuestion.updatePosition();

        //colisiones

        //actualizar flappy
        if(!this.bouncyView.isLanded()) {
            this.bouncyView.updateState();
        }

        //actualizar explosion
        if(this.bouncyView.isLanded()) {
            //se carga el view de la explosión para aterrizaje del flappy o choque
            this.explosionView.updateState();
        }

        //aquí se comprobará la condición de final de juego

    }
    public void sceneLoad(){
        int[] backgroundScenes = this.scene.getBackgroundScenes();
        imagenes=new Bitmap[backgroundScenes.length];
        //cargamos todos los fondos en un array
        for(int i=0;i<backgroundScenes.length;i++) {
            Bitmap ambientRes = BitmapFactory.decodeResource(getResources(), backgroundScenes[i]);
            if(imagenes[i]==null)
                imagenes[i] = ambientRes.createScaledBitmap(ambientRes,
                        this.play.getScene().getScreenWidth(),
                        this.play.getScene().getScreenHeight(), true);
            ambientRes.recycle();
        }
    }
    public void questionLoad(){
        GameConfig gameConfig = this.gameActivity.getGameConfig();
        gameConfig.setFramesToNewQuestion(GameLoop.MAX_FPS*60/ gameConfig.getMinutesToQuestion());
        Question question = new Question();
        QuestionView goQuestion = new QuestionView(this.play, question);
        if(goQuestion.getQuestion().getComplejidad()== GameUtil.PREGUNTA_COMPLEJIDAD_ALTA)
            this.goDificultQuestion = goQuestion;
        else
            this.goEasyQuestion = goQuestion;
    }
    public void createNewQuestion(){
        if(this.gameActivity.getGameConfig().getQuestions()-
            this.play.getQuestionsCreated()>0){
            this.play.getQuestionViews().add(new QuestionView(this.play,new Question()));
            this.play.setQuestionsCreated(this.play.getQuestionsCreated()+1);
        }
    }
    public void updateSceneBackground(){
        //nueva posición del fondo
        this.scene.setxCurrentImg(this.scene.getxCurrentImg()-1);
        this.scene.setxNextImg(this.scene.getxNextImg()-1);

        /*Si la imagen de fondo actual ya ha bajado completamente*/
        if(this.scene.getxCurrentImg()< -this.screenWidth){

            //Se actualiza la imagen actual a la siguiente del array de imagenes
            if(this.scene.getCurrentImgIndex()==this.scene.getQuestionViewImgNumber()-1)
                this.scene.setCurrentImgIndex(0);
            else
                this.scene.setCurrentImgIndex(this.scene.getCurrentImgIndex()+1);

            //Se actualiza la imagen siguiente
            if(this.scene.getNextImgIndex()==this.scene.getQuestionViewImgNumber()-1)
                this.scene.setNextImgIndex(0);
            else
                this.scene.setNextImgIndex(this.scene.getNextImgIndex()+1);

            //Nuevas coordenadas
            this.scene.setxCurrentImg(this.screenWidth);
            this.scene.setxNextImg(0);
        }
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // se crea la superficie, creamos el game loop

        // Para interceptar los eventos de la SurfaceView
        getHolder().addCallback(this);

        // creamos el game loop
        gameLoop = new GameLoop(getHolder(), this);

        // Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        //comenzar el bucle
        gameLoop.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        // cerrar el thread y esperar que acabe
        boolean retry = true;
        while (retry) {
            try {
                //aquí invocaremos al método para detener el juego
                gameLoop.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    public BouncyView getBouncyView() {
        return bouncyView;
    }

    public ExplosionView getExplosionView() {
        return explosionView;
    }
    public Play getPlay() {
        return play;
    }

    public Scene getScene() {
        return scene;
    }

    public GameActivity getGameActivity(){
        return this.gameActivity;
    }
    public AudioController getAudioController(){
        return this.audioController;
    }
}
