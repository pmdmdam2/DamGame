package dam.gala.damgame.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import dam.gala.damgame.model.GameConfig;
import dam.gala.damgame.scenes.Scene;

import java.util.Iterator;

/**
 * Personaje principal, debe sortear obstáculos y responder el máximo número de preguntas
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class BouncyView {
    private int spriteWidth;
    private int spriteHeight;
    public int xCoord, yCoord, yCurrentCoord;
    private int offset;
    private int velocidadVertical;
    private Bitmap bouncyBitmap;
    private int gravity;
    private boolean finished;
    private boolean landed;
    private boolean collision;
    private boolean questionCatched;
    private GameView gameView;
    private GameConfig gameConfig;
    private QuestionView questionViewCatched;

    /**
     * Construye el OGP a partir del coreógrafo de la escena
     * @param gameView Coreógrafo de la escena
     */
    public BouncyView(GameView gameView) {
        this.gameView = gameView;
        Scene scene = this.gameView.getScene();
        gameConfig = gameView.getGameActivity().getGameConfig();
        this.yCoord = scene.getScreenHeight() / 2 - scene.getBouncyViewHeight() / 2;
        this.xCoord = scene.getBouncyViewWidth() / 5;
        this.yCurrentCoord = yCoord;
        this.spriteWidth = scene.getBouncyViewWidth() / scene.getBouncyViewImgNumber();
        this.spriteHeight = scene.getBouncyViewHeight();
        this.velocidadVertical = scene.getScreenWidth() * 10 / 1080;
        this.bouncyBitmap = scene.getBouncyViewBitmap();
        this.gravity = this.gameConfig.getGravity();

        offset = -1; //recien creado
        //creación y control del sonido asociado al movimiento del objeto principal
    }

    /**
     * Actualiza el estado del OGP
     */
    public void updateState() {
        if(this.finished) {
            this.landed = false;
            return;
        }
        if (gravity > (this.yCoord)) {
            this.gravity = 0;
            this.offset = -1;
            this.finished = true;
            this.landed = true;
        } else {
            if (this.offset == 3)
                this.offset = -1;
        }
        //---------------------------------------------------------------------------------------
        //TODO ACTIVIDAD 4.11
        //---------------------------------------------------------------------------------------
        //TODO Choque con alguna columna
        //Se comprueba el OGP ha chocado con el OGP. Para ello hay que recorrer todos los
        //bloques que hay en escena y se comprueba si hay intersección entre el OGP y algún bloque
        //Si hay colisión se registra en la propiedad colisión

        //---------------------------------------------------------------------------------------
        //TODO ACTIVIDAD 4.12
        //---------------------------------------------------------------------------------------
        //TODO Captura de una pregunta
        //Se comprueba si el OGP ha capturado (chocado) con alguna de las preguntas creadas (visual)
        //Para ello hay que recorrer todas las preguntas creadas y comprobar si el área del OGP se
        //intersecciona con el área de alguna pregunta
        //Si hay captura habrá que eliminar el objeto visual de las preguntas creadas
    }

    public void draw(Canvas canvas, Paint paint) {
        //terminará la animación de movimiento cuando se capture una pregunta
        //, se choque con un obstáculo o se caiga al suelo
        if (isFinished())
            return;

        this.offset++;
        //se calcula el area del bitmap que se va a dibujar
        Rect startRect = new Rect(this.offset * this.spriteWidth, 0,
                (this.offset + 1) * this.spriteWidth
                , this.spriteHeight);
        Rect endRect = new Rect(this.xCoord, this.yCoord + this.gravity, this.xCoord +
                this.spriteWidth
                , this.yCoord + this.spriteHeight + this.gravity);
        canvas.drawBitmap(this.getBouncyBitmap(), startRect, endRect, paint);
        //comentar las líneas de abajo para quitar la gravedad
        this.gravity+=this.gameConfig.getGravity();
        this.yCurrentCoord = this.yCoord + this.gravity;
    }

    /**
     * Tererminará la animación de movimiento cuando se capture una pregunta
     * o se choque con un obstáculo
     *
     * @return boolean Valor true significa que se debe detener la animación
     */
    public boolean isFinished() {
        return this.landed || this.collision || this.questionCatched;
    }

    public int getxCoord() {
        return this.xCoord;
    }

    public int getyCoord() {
        return this.yCoord;
    }

    public int getyCurrentCoord(){
        return this.yCurrentCoord;
    }

    public int getOffset() {
        return offset;
    }

    public float getVelocidadVertical() {
        return velocidadVertical;
    }

    public Bitmap getBouncyBitmap() {
        return this.bouncyBitmap;
    }

    public boolean isLanded() {
        return this.landed;
    }
    public boolean isColluded(){
        return this.collision;
    }
    public boolean isQuestionCatched(){
        return this.questionCatched;
    }
    public QuestionView getQuestionCatched(){
        return this.questionViewCatched;
    }
    public void setQuestionViewCatched(QuestionView questionViewCatched){
        this.questionViewCatched = questionViewCatched;
    }
}
