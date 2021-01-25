package com.example.damgame.controllers;

import android.view.MotionEvent;
import android.view.View;
import com.example.damgame.activities.GameActivity;
import com.example.damgame.model.Touch;
import java.util.ArrayList;

/**
 * Controlador para el toque de pantalla
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class TouchController implements View.OnTouchListener{
    private GameActivity gameActivity;
    private boolean touched=false;
    private ArrayList<Touch> touches = new ArrayList<Touch>();

    public TouchController(GameActivity gameActivity){
        this.gameActivity = gameActivity;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int index;
        int x,y;
        // Obtener el pointer asociado con la acción
        index = motionEvent.getActionIndex();
        x = (int) motionEvent.getX();
        y = (int) motionEvent.getY();
        switch(motionEvent.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                touched=true;
                synchronized(this) {
                    touches.add(index, new Touch(index, x, y));
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                synchronized(this) {
                    touches.remove(index);
                }
                break;
            case MotionEvent.ACTION_UP:
                synchronized(this) {
                    touches.clear();
                }
                touched=false;
                break;
        }
        return true;
    }
    public boolean isTouched(){
        return this.touched;
    }
    public ArrayList<Touch> getTouches(){
        return this.touches;
    }
}
