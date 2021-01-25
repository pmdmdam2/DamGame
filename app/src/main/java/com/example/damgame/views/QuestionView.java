package com.example.damgame.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.example.damgame.model.Play;
import com.example.damgame.model.Question;

/**
 * Pregunta
 * @author 2º DAM - IES Antonio Gala
 * @version  1.0
 */
public class QuestionView {
    private int anchoSprite;
    private int altoSprite;
    //coordenadas donde se dibuja el control
    public float coordenadaX, coordenadaY;
    private int estado;
    private Play play;
    private Scene scene;
    private Bitmap questionBitmap;
    private Question question;
    private int verticalDirection;
    private int horizontalDirection;
    private int speed;

    public QuestionView(Play play, Question question){
        this.play = play;
        this.scene = play.getScene();
        this.question = question;
        anchoSprite = this.scene.getQuestionViewWidth()/this.scene.getQuestionViewImgNumber();
        altoSprite =this.scene.getQuestionViewHeight();
        estado=-1; //recien creado
        this.questionBitmap = play.getScene().getQuestionViewBitmap(question.getComplejidad());
        //creación y control del sonido de la captura de la pregunta
    }
    public QuestionView(Play play, Question question, float x, float y){
        this(play, question);
        this.coordenadaX =x;
        this.coordenadaY =y;
    }
    public void updateState(){
        //incrementamos el estado al siguiente momento de la explosión
        estado++;
    }
    public void updatePosition(){
        this.coordenadaX += this.horizontalDirection * this.speed;
        this.coordenadaY += this.verticalDirection * this.speed;
        //Cambios de direcciones al llegar a los bordes de la pantalla
        if(coordenadaX <=0 && this.horizontalDirection ==-1)
            this.horizontalDirection =1;
        if(coordenadaX >this.scene.getScreenWidth()-
                this.questionBitmap.getWidth() &&
                this.horizontalDirection ==1)
            this.horizontalDirection =-1;

        if(coordenadaY >=this.scene.getScreenHeight() && this.verticalDirection ==1)
            this.verticalDirection =-1;
        if(coordenadaY <=0 && this.verticalDirection ==-1)
            this.verticalDirection =1;
    }
    public void draw(Canvas canvas, Paint paint){
        int posicionSprite=estado* anchoSprite;

        if(!isFinished()) {
            //Calculamos el cuadrado del sprite que vamos a dibujar
            Rect origen = new Rect(posicionSprite, 0, posicionSprite + anchoSprite,
                    altoSprite);

            //calculamos donde vamos a dibujar la porcion del sprite
            Rect destino = new Rect((int) coordenadaX,(int) coordenadaY,(int) coordenadaX + anchoSprite,
                    (int) coordenadaY + this.altoSprite);

            canvas.drawBitmap(play.getScene().getQuestionViewBitmap(this.question.getComplejidad()), origen, destino, paint);
        }

    }
    public boolean isFinished(){
        return estado>=this.scene.getQuestionViewImgNumber();
    }

    public Question getQuestion() {
        return question;
    }
}
