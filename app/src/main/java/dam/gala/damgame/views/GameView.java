package dam.gala.damgame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.damgame.R;

import dam.gala.damgame.activities.GameActivity;
import dam.gala.damgame.controllers.AudioController;
import dam.gala.damgame.controllers.TouchController;
import dam.gala.damgame.model.GameConfig;
import dam.gala.damgame.model.Play;
import dam.gala.damgame.model.Question;
import dam.gala.damgame.model.Touch;
import dam.gala.damgame.scenes.Scene;
import dam.gala.damgame.threads.GameLoop;
import dam.gala.damgame.utils.GameUtil;

import java.util.Iterator;

/**
 * Vista principal del juego, gestiona todos los personajes, objetos y escenas.
 *
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final int SCORE_POINTS=10;
    private final int SCORE_LIFES=11;
    private final int SCORE_ANSWERS=12;
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
    //Pregunta capturada
    private QuestionExplosionView questionExplosionView;
    //Imagen de escena
    private Scene scene;
    //imágenes para preguntas
    private Bitmap[] imagenes;
    //Controlador de toque en la pantalla
    private TouchController touchController;
    //Controlador de audio
    private AudioController audioController;
    //Parar el juego
    private boolean stopGame;

    public GameView(Context context) {
        super(context);
    }
    /**
     * Construye el objeto que actúa como coreógrafo de la escena del juego
     * @param context Actividad principal del juego
     * @param attrs Atributos de la vista del juego
     */
    public GameView(Context context, AttributeSet attrs) {
        super((Context) context,attrs);
        this.gameActivity = (GameActivity) context;
        this.touchController = new TouchController(this.gameActivity);
        this.audioController = new AudioController(this.gameActivity);
        this.gameConfig = gameActivity.getGameConfig();
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.play = gameActivity.getJugada();
        this.scene = this.play.getScene();
        this.gameConfig.setMinHeightCrashBlock((int) (this.scene.getScreenHeight() * 0.1));
        //se obtiene el tamaño de la pantalla
        this.play.getScene().getScreenSize(this.gameActivity);
        this.screenWidth = this.play.getScene().getScreenWidth();
        this.screenHeight = this.play.getScene().getScreenHeight();
        //se carga el OGP y se calcula su posición inicial
        this.bouncyView = new BouncyView(this);
        //explosion, cuando el flappy choca o cae al suelo
        this.explosionView = new ExplosionView(this);
        //captura de una pregunta
        //se carga el efecto animado de la captura de la pregunta
        this.questionExplosionView = new QuestionExplosionView(this);

        //ambiente
        this.sceneLoad();
        //preguntas
        this.questionsConfig();
        //se controlan los toques en pantalla
        this.setOnTouchListener(this.touchController);
    }

    /**
     * Dibujar el estado actual de la escena
     * @param canvas Lienzo de dibujo
     */
    public void render(Canvas canvas) {
        Scene scene = this.play.getScene();
        if (canvas != null) {
            //pinceles
            Paint myPaint = new Paint();
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setColor(Color.WHITE);

            Paint myPaint2 = new Paint();
            myPaint2.setStyle(Paint.Style.FILL);
            myPaint2.setTextSize(50);

            //dibujamos el fondo
            canvas.drawBitmap(imagenes[this.scene.getCurrentImgIndex()], this.scene.getxCurrentImg(), 0, null);
            canvas.drawBitmap(imagenes[this.scene.getNextImgIndex()], this.scene.getxNextImg(), 0, null);

            //Si ha ocurrido un toque en la pantalla "Touch", dibujar un círculo
            if (this.touchController.isTouched()) {
                synchronized (this) {
                    for (Touch t : this.touchController.getTouches()) {
                        canvas.drawCircle(t.getX(), t.getY(), 100, myPaint);
                        //canvas.drawText(t.index + "", t.x, t.y, myPaint2);
                    }
                }
            }
            if (this.isStopGame()) {
                this.audioController.stopAudioPlay();
                if(this.play.isFinished()){
                    //poner sonido de final de juego y mostrar ranking
                }else if(this.bouncyView.isLanded() || this.bouncyView.isColluded()){
                    //se vuelve a empezar el juego con la siguiente vida
                    this.audioController.startAudioPlay(this.getScene());
                    this.updateScore(SCORE_LIFES,String.valueOf(this.play.getLifes()));
                    this.bouncyView.reStart();
                }else if(this.bouncyView.isQuestionCatched()){
                    //poner audio para la pregunta
                    //mostrar cuadro de diálogo para la pregunta
                }
            }

            //dibuja el OGP (posición inicial a 1/5 de ancho y la mitad de alto)
                /*canvas.drawBitmap(this.bouncyView.getBouncyBitmap(), this.bouncyView.getCoordenadaX(),
                        this.bouncyView.getCoordenadaY(), null);*/

            if (this.bouncyView.isQuestionCatched()) {
                this.questionExplosionView.draw(canvas, myPaint);
                if (!this.audioController.isMediaExplosionStarted()) {
                    this.audioController.startAudioQuestionExplosion(this.getScene());
                }
            }

            if (this.bouncyView.isLanded() || this.bouncyView.isColluded()) {
                this.explosionView.draw(canvas, myPaint);
                if (!this.audioController.isMediaExplosionStarted()) {
                    this.audioController.startAudioExplosion(this.getScene());
                }
            } else {
                this.bouncyView.draw(canvas, myPaint);
            }

            for (QuestionView questionView : this.play.getQuestionViews())
                questionView.draw(canvas, myPaint);

            for (CrashView crashView : this.play.getCrashViews())
                crashView.draw(canvas, myPaint);


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
        if (this.isStopGame()) return;

        this.updateSceneBackground();

        if (this.gameConfig.getFramesToNewCrashBlock() == 0) {
            if (this.play.getCrashViews().size() < this.gameConfig.getCrashBlocks())
                this.createNewCrashBlock();
            //nuevo ciclo de bloques
            this.gameConfig.setFramesToNewCrashBlock(GameLoop.MAX_FPS * 60 /
                    this.gameConfig.getTimeToCrashBlock());
        }
        this.gameConfig.setFramesToNewCrashBlock(this.gameConfig.getFramesToNewCrashBlock() - 1);
        //los bloques aparecen y se mueven
        Iterator iterator = this.play.getCrashViews().iterator();
        CrashView crashView;
        while (iterator.hasNext()) {
            crashView = (CrashView) iterator.next();
            crashView.updateState();
            if (crashView.getxCoor() <= (-crashView.getWidth())) {
                iterator.remove();
                this.play.setCrashBlockCreated(this.play.getCrashBlockCreated() - 1);
            }
        }

        if (this.gameConfig.getFramesToNewQuestion() == 0) {
            this.createNewQuestion();
            //nuevo ciclo de preguntas
            this.gameConfig.setFramesToNewQuestion(GameLoop.MAX_FPS * 60 / this.gameConfig.
                    getTimeToQuestion());
        }
        this.gameConfig.setFramesToNewQuestion(this.gameConfig.getFramesToNewQuestion() - 1);

        for (QuestionView goQuestion : this.play.getQuestionViews())
            goQuestion.updatePosition();
        //-----------------------------------------------------------------------------------------

        //caputura de las preguntas

        //actualizar flappy
        if (!this.bouncyView.isLanded()) {
            this.bouncyView.updateState();
        }

        if (this.bouncyView.isLanded() || this.bouncyView.isColluded()) {
            //se carga el view de la explosión para aterrizaje del flappy o choque
            this.explosionView.updateState();
        }

        if (this.bouncyView.isQuestionCatched()) {
            this.questionExplosionView.updateState();
        }

        //aquí se comprobará la condición de final de juego

    }
    /**
     * Carga la escena
     */
    public void sceneLoad() {
        int[] backgroundScenes = this.scene.getBackgroundScenes();
        imagenes = new Bitmap[backgroundScenes.length];
        //cargamos todos los fondos en un array
        for (int i = 0; i < backgroundScenes.length; i++) {
            Bitmap ambientRes = BitmapFactory.decodeResource(getResources(), backgroundScenes[i]);
            if (imagenes[i] == null)
                imagenes[i] = Bitmap.createScaledBitmap(ambientRes,
                        this.play.getScene().getScreenWidth(),
                        this.play.getScene().getScreenHeight(), true);
            ambientRes.recycle();
        }
    }

    public void createNewCrashBlock() {
        if (this.gameActivity.getGameConfig().getCrashBlocks() -
                this.play.getCrashBlockCreated() > 0) {
            TopCrashView topCrashView = new TopCrashView(this);
            this.play.getCrashViews().add(topCrashView);
            this.play.getCrashViews().add(new DownCrashView(this, topCrashView));
            this.play.setCrashBlockCreated(this.play.getCrashBlockCreated() + 1);
        }
    }

    public void questionsConfig() {
        GameConfig gameConfig = this.gameActivity.getGameConfig();
        gameConfig.setFramesToNewQuestion(GameLoop.MAX_FPS * 60 / gameConfig.getTimeToQuestion());
    }

    public void createNewQuestion() {
        if (this.gameActivity.getGameConfig().getQuestions() -
                this.play.getQuestionsCreated() > 0) {
            Question question = new Question();
            QuestionView goQuestion = new QuestionView(this.play, question);
            this.play.getQuestionViews().add(goQuestion);
            this.play.setQuestionsCreated(this.play.getQuestionsCreated() + 1);
        }
    }
    /**
     * Actualiza la posición de la imagen de fondo en la escena
     */
    public void updateSceneBackground() {
        //nueva posición del fondo
        this.scene.setxCurrentImg(this.scene.getxCurrentImg() - 1);
        this.scene.setxNextImg(this.scene.getxNextImg() - 1);

        /*Si la imagen de fondo actual ya ha bajado completamente*/
        if (this.scene.getxCurrentImg() < -this.screenWidth) {

            //Se actualiza la imagen actual a la siguiente del array de imagenes
            if (this.scene.getCurrentImgIndex() == this.scene.getQuestionViewImgNumber() - 1)
                this.scene.setCurrentImgIndex(0);
            else
                this.scene.setCurrentImgIndex(this.scene.getCurrentImgIndex() + 1);

            //Se actualiza la imagen siguiente
            if (this.scene.getNextImgIndex() == this.scene.getQuestionViewImgNumber() - 1)
                this.scene.setNextImgIndex(0);
            else
                this.scene.setNextImgIndex(this.scene.getNextImgIndex() + 1);

            //Nuevas coordenadas
            this.scene.setxCurrentImg(this.screenWidth);
            this.scene.setxNextImg(0);
        }
    }

    /**
     * Comprueba si el juego ha finalizado
     * @return Devuelve true si el juego ha finalizado
     */
    public boolean isStopGame() {
        return this.play.isFinished() || this.stopGame;
    }

    /**
     * Actualiza el estado de finalización del juego
     * @param stopGame
     */
    public void setStopGame(boolean stopGame) {
        this.stopGame = stopGame;
    }

    /**
     * Se crea la superficie de dibujo. Aquí se inicia el bucle de juego.
     * @param surfaceHolder
     */
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

    /**
     * Se destruye la superficie de dibujo del juego. El juego finaliza
     * @param surfaceHolder
     */
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

    /* Métodos getter y setter */

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

    public GameActivity getGameActivity() {
        return this.gameActivity;
    }

    public AudioController getAudioController() {
        return this.audioController;
    }
    private void updateScore(final int what, final String text){
        this.gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               switch (what){
                   case SCORE_ANSWERS:
                       break;
                   case SCORE_LIFES:
                       GameView.this.gameActivity.setTextTvLifes(text);
                       break;
                   case SCORE_POINTS:
                       break;
               }
            }
        });
    }

}
