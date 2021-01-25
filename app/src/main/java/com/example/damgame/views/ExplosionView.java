package com.example.damgame.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Explosión que se produce cuando flappy cae al suelo
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class ExplosionView {
    private int spriteWidth;
    private int spriteHeight;
    public int xCoord, yCoord;
    private int offset;
    private GameView gameView;
    private int landscapeSpeed;
    private Bitmap explosionBitmap;
    private boolean finished;
    private int estado;
    private BouncyView bouncyView;
    //reproduce explosión

    /*Constructor con coordenadas iniciales y número de explosión*/
    public ExplosionView(GameView gameView){
        this.gameView = gameView;
        Scene scene = this.gameView.getScene();
        //this.yCoord = bouncyView.yCoord;//this.scene.getScreenHeight() /2-this.scene.getExplosionViewHeight()/2;
       // this.xCoord = bouncyView.xCoord;//this.scene.getExplosionViewWidth() /5;
        this.spriteWidth = scene.getExplosionViewWidth()/scene.getExplosionViewImgNumber();
        this.spriteHeight = scene.getExplosionViewHeight();
        this.landscapeSpeed=  scene.getScreenWidth()  *10/1080;
        this.explosionBitmap = scene.getExplosionViewBitmap();
        offset = -1; //recien creado
    }

    public void updateState(){
        if(drawFinished()) return;
        //incrementamos el estado al siguiente momento de la explosión
        this.offset++;
    }

    public void draw(Canvas canvas, Paint paint){
        if(!drawFinished()) {
            this.yCoord = this.gameView.getBouncyView().getyCurrentCoord();
            this.xCoord = this.gameView.getBouncyView().getxCoord();
            //Calculamos el cuadrado del sprite que vamos a dibujar
            Rect startRect = new Rect(this.offset* this.spriteWidth,
                    0, (this.offset * this.spriteWidth) + this.spriteWidth,
                    this.spriteHeight);

            //calculamos donde vamos a dibujar la porcion del sprite
            Rect endRect = new Rect((int) this.xCoord,(int) this.yCoord,
                    (int) this.xCoord + this.spriteWidth,
                    (int) this.yCoord + this.spriteHeight);

            canvas.drawBitmap(this.explosionBitmap,startRect,endRect,paint);
        }

    }
    public boolean drawFinished(){
        return this.estado>=this.gameView.getScene().getExplosionViewImgNumber();
    }
    public Bitmap getExplosionBitmap(){
        return this.explosionBitmap;
    }
}
