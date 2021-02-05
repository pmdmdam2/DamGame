package com.example.damgame.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.example.damgame.model.GameConfig;

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
    private boolean crash;
    private GameView gameView;
    private GameConfig gameConfig;


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
    }

    public void draw(Canvas canvas, Paint paint) {
        //terminará la animación de movimiento cuando se capture una pregunta
        //, se choque con un obstáculo o se caiga al suelo
        if (drawFinished())
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

        this.gravity+=this.gameConfig.getGravity();
        this.yCurrentCoord = this.yCoord + this.gravity;
    }

    /**
     * Tererminará la animación de movimiento cuando se capture una pregunta
     * o se choque con un obstáculo
     *
     * @return boolean Valor true significa que se debe detener la animación
     */
    public boolean drawFinished() {
        return this.landed || this.crash;
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
        return landed;
    }

}
