package com.example.damgame.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import com.example.damgame.R;
import com.example.damgame.activities.GameActivity;
import com.example.damgame.utils.GameUtil;

/**
 * Escena del desierto
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class DesertScene extends Scene {
    private GameActivity gameActivity;

    public DesertScene(GameActivity gameActivity){
        super(gameActivity);
        this.gameActivity = gameActivity;
        this.backgroundScenes = new int[]{this.getBouncyBitmapId(),this.getBouncyBitmapId()
                ,this.getBouncyBitmapId(),
                this.getBouncyBitmapId(),
                this.getBouncyBitmapId(),
                this.getBouncyBitmapId()};
        this.xCurrentImg=0;
        this.xNextImg= this.getScreenWidth()-1;
    }
    @Override
    public int getQuestionViewWidth() {
        return 100;
    }
    @Override
    public int getQuestionViewHeight() {
        return 100;
    }

    @Override
    public Bitmap getQuestionViewBitmap(int complexity) {
        if(complexity== GameUtil.PREGUNTA_COMPLEJIDAD_ALTA)
            return BitmapFactory.decodeResource(gameActivity.getResources(), R.drawable.desert_quest_d);
        else
            return BitmapFactory.decodeResource(gameActivity.getResources(), R.drawable.desert_quest_e);
    }

    @Override
    public int getQuestionViewImgNumber() {
        return 4;
    }

    @Override
    public int getBouncyViewWidth() {
        return this.gameActivity.getResources().getDrawable(R.drawable.desert_hatbird).getMinimumWidth();
    }

    @Override
    public int getBouncyViewHeight() {
        return this.gameActivity.getResources().getDrawable(R.drawable.desert_hatbird).getMinimumHeight();
    }

    @Override
    public Bitmap getBouncyViewBitmap() {
        return BitmapFactory.decodeResource(gameActivity.getResources(), R.drawable.desert_hatbird);
    }

    @Override
    public int getBouncyViewImgNumber() {
        return 4;
    }

    @Override
    public int getBouncyBitmapId() {
        return R.drawable.desert_bg;
    }

    @Override
    public int[] getBackgroundScenes() {
        return backgroundScenes;
    }

    @Override
    public int getxCurrentImg() {
        return xCurrentImg;
    }

    @Override
    public int getxNextImg() {
        return xNextImg;
    }

    @Override
    public void setxCurrentImg(int xCurrentImg) {
        this.xCurrentImg = xCurrentImg;
    }

    @Override
    public void setxNextImg(int xNextImg) {
        this.xNextImg = xNextImg;
    }

    @Override
    public int getCurrentImgIndex() {
        return this.currentImgIndex;
    }

    @Override
    public void setCurrentImgIndex(int currentImgIndex) {
        this.currentImgIndex = currentImgIndex;
    }

    @Override
    public int getNextImgIndex() {
        return this.nextImgIndex;
    }

    @Override
    public void setNextImgIndex(int nextImgIndex) {
        this.nextImgIndex = nextImgIndex;
    }

    @Override
    public int getExplosionViewWidth() {
        return this.gameActivity.getResources().getDrawable(R.drawable.explosion_out).getMinimumWidth();
    }

    @Override
    public int getExplosionViewHeight() {
        return this.gameActivity.getResources().getDrawable(R.drawable.explosion_out).getIntrinsicHeight();
    }

    @Override
    public int getExplosionBitmapId() {
        return R.drawable.explosion_out;
    }

    @Override
    public int getExplosionViewImgNumber() {
        return 4;
    }

    @Override
    public Bitmap getExplosionViewBitmap() {
        return BitmapFactory.decodeResource(gameActivity.getResources(), R.drawable.explosion_out);
    }

    @Override
    public View getCharacterView() {
        return null;
    }

    @Override
    public int getAudioPlay() {
        return R.raw.my_street;
    }

    @Override
    public int getAudioExplosion() {
        return R.raw.explosion;
    }

    @Override
    public int getAudioCrash() {
        return R.raw.crash;
    }
}
