package dam.gala.damgame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import dam.gala.damgame.activities.GameActivity;
import dam.gala.damgame.controllers.AudioController;
import dam.gala.damgame.controllers.TouchController;
import dam.gala.damgame.model.GameConfig;
import dam.gala.damgame.model.Play;
import dam.gala.damgame.model.Question;
import dam.gala.damgame.model.Touch;
import dam.gala.damgame.scenes.Scene;
import dam.gala.damgame.threads.GameLoop;

import java.util.Iterator;

/**
 * Vista principal del juego, gestiona todos los personajes, objetos y escenas.
 *
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
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
    private QuestionView goEasyQuestion;
    private QuestionView goDificultQuestion;
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
     * @param gameActivity Actividad principal del juego
     */
    public GameView(@NonNull GameActivity gameActivity) {
        super(gameActivity);
        this.gameActivity = gameActivity;
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
        this.questionExplosionView = new QuestionExplosionView(this);
        //se carga el efecto animado de la captura de la pregunta

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
                }else if(this.bouncyView.isQuestionCatched()){
                    //poner audio para la pregunta
                    //mostrar cuadro de diálogo para la pregunta
                }
            }

            //---------------------------------------------------------------------------------------
            //TODO ACTIVIDAD 4.12
            //---------------------------------------------------------------------------------------
            //TODO Captura de una pregunta
            //Se comprueba si el OGP ha capturado la pregunta. Si es así mostramos la explosión
            //con su animación y el audio correspondiente

            //---------------------------------------------------------------------------------------
            //TODO ACTIVIDAD 4.11
            //---------------------------------------------------------------------------------------
            //TODO Choque con alguna columna
            //Podemos comprobar el choque del OGP con una columna o con el suelo. Si es así
            //debemos mostrar la animación de la explosión y el sonido correspondiente. En caso
            //contrario seguimos mostrando el OGP en su posición inicial

            //--------------------------------------------------------------------------------------
            //TODO Actividad 4.9 PMDM
            //--------------------------------------------------------------------------------------
            //TODO Dibuja las preguntas
            //Debes recorrer todas las preguntas que se han creado y dibujarlas

            //--------------------------------------------------------------------------------------
            //TODO Actividad 4.10 PMDM
            //--------------------------------------------------------------------------------------
            //TODO Dibuja las bloques de choque
            //Debes recorrer todos los bloques de choque crados dibujarlos


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

        //-----------------------------------------------------------------------------------------
        //TODO Actividad 4.10 PMDM
        //-----------------------------------------------------------------------------------------
        //TODO Actualizar el estado de los bloques de choque
        //Si no se han creado todos los bloques de choque se crea uno nuevo y se cuenta como
        //bloque creado

        //Ahora tenemos que comprobar si un bloque ha llegado al margen izquierdo de la pantalla
        //en dicho caso se elimina el bloque para que en la siguiente actualización del juego
        //se vuelva a crear un nuevo bloque por la derecha.

        //-----------------------------------------------------------------------------------------
        //TODO Actividad 4.9 PMDM
        //-----------------------------------------------------------------------------------------
        //TODO se calculan los frames para crear una nueva pregunta
        //Si el número de frames para la nueva pregunta es 0, se crea la nueva pregunta, y se
        //actualiza el número de marcos para crear la siguiente pregunta. Después se descuenta el
        //frame actual

        //-----------------------------------------------------------------------------------------
        //TODO Actividad 4.9 PMDM
        //-----------------------------------------------------------------------------------------
        //TODO Se muestran los objetos gráficos de las preguntas
        //Debes recorrer todas las preguntas creadas y actualizar el estado de las mismas

        //-----------------------------------------------------------------------------------------

        //actualizar OGP si no ha tocado suelo y no ha chocado con una columna
        if (!this.bouncyView.isLanded() || !this.bouncyView.isColluded()) {
            this.bouncyView.updateState();
        }
        //-----------------------------------------------------------------------------------------
        //TODO Actividad 4.11 PMDM
        //-----------------------------------------------------------------------------------------
        //TODO Actualizar el objeto visual de la explosión tras chocar con una columna
        //o tras caer al suelo


        //-----------------------------------------------------------------------------------------
        //TODO Actividad 4.12 PMDM
        //-----------------------------------------------------------------------------------------
        //TODO Actualizar la destrucción de la pregunta una vez capturada
        //mostrar el cuadro de diálogo de la pregunta y detener el juego




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

    //-----------------------------------------------------------------------------------------
    //TODO Actividad 4.10 PMDM
    //-----------------------------------------------------------------------------------------
    //TODO crea un nuevo bloque de choque
    public void createNewCrashBlock() {
        //tendrás que comprobar si el número de bloques creados menos el número máximo de bloques
        //definidos en la configuración del juego es mayor que 0
        //Si lo anterior es cierto, será necesario crear el bloque superior e inferior y
        //añadirlos la lista de bloques creados. Además se actualizarán el número de bloques
        //Por cada 2 bloques creados (superior e inferior) contamos sólo 1
    }

    //-----------------------------------------------------------------------------------------
    //TODO Actividad 4.9 PMDM
    //-----------------------------------------------------------------------------------------
    //TODO se configura el tiempo de aparición de cada pregunta
    public void questionsConfig() {
        //Se debe calcular el número de marcos por segundo que deben producirse antes de
        //crear una nueva pregunta. Para ello es necesario saber el número máximo de FPS
        // y el tiempo que debe transcurrir para una nueva pregunta.
    }

    //-----------------------------------------------------------------------------------------
    //TODO Actividad 4.9 PMDM
    //-----------------------------------------------------------------------------------------
    //TODO Se añade una nueva pregunta al array de preguntas
    public void createNewQuestion() {
        //Crearemos la pregunta visual comprobando la complejidad de la misma. Cuando
        //sepamos la complejidad almacenaremos la referencia de la pregunta en la propiedad
        //correspondiente. Se debe diferenciar entre preguntas con complejidad simple y compleja
        //para mostrar el bitmap correspondiente.
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
}
