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
    private int spriteWidth;
    private int spriteHeight;
    public float xCoor, yCoor;
    private int spriteIndex;
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
        spriteWidth = this.scene.getQuestionViewWidth()/this.scene.getQuestionViewImgNumber();
        spriteHeight =this.scene.getQuestionViewHeight();
        spriteIndex =-1; //recien creado
        this.questionBitmap = play.getScene().getQuestionViewBitmap(question.getComplejidad());
        //asociación del sonido de la captura de la pregunta

        //-----------------------------------------------------------------------------------------
        //Actividad 4.9 PMDM
        //-----------------------------------------------------------------------------------------

        //la probabilidad de generar una pregunta sencilla es 80%,
        //y de una pregunta difícil el 20%

        //cálculo de dirección aleatoria de cada pregunta generada

        //posicionamiento del objeto gráfico de la pregunta. Por ejemplo, probabilidad <= 33%, la
        //pregunta sale por arriba. Entre 33 y 66% aparece por la parte central. Si es > 66% el
        //objeto gráfico de la pregunta aparece por la parte de abajo.
        //-----------------------------------------------------------------------------------------


    }
    public QuestionView(Play play, Question question, float x, float y){
        this(play, question);
        this.xCoor =x;
        this.yCoor =y;
    }

    public void updatePosition(){
        //-----------------------------------------------------------------------------------------
        //Actividad 4.9 PMDM
        //-----------------------------------------------------------------------------------------
        //se calcula la nueva posición de la pregunta. Revisar el siguiente código
        this.xCoor += this.horizontalDirection * this.speed;
        this.yCoor += this.verticalDirection * this.speed;
        //Cambios de direcciones al llegar a los bordes de la pantalla
        if(xCoor <=0 && this.horizontalDirection ==-1)
            this.horizontalDirection =1;
        if(xCoor >this.scene.getScreenWidth()-
                this.questionBitmap.getWidth() &&
                this.horizontalDirection ==1)
            this.horizontalDirection =-1;

        if(yCoor >=this.scene.getScreenHeight() && this.verticalDirection ==1)
            this.verticalDirection =-1;
        if(yCoor <=0 && this.verticalDirection ==-1)
            this.verticalDirection =1;
        //-----------------------------------------------------------------------------------------
    }
    public void draw(Canvas canvas, Paint paint){
        int spritePosition=0;
        //-----------------------------------------------------------------------------------------
        //Actividad 4.9 PMDM
        //-----------------------------------------------------------------------------------------
        //calcular el nuevo índice del sprite para la animación de la pregunta

        //calcular la posición del sprite en la pantalla

        if(!isFinished()) {
            //Calculamos el cuadrado del sprite que vamos a dibujar
            Rect origen = new Rect(spritePosition, 0, spritePosition + spriteWidth,
                    spriteHeight);

            //calculamos donde vamos a dibujar la porcion del sprite
            Rect destino = new Rect((int) xCoor,(int) yCoor,(int) xCoor + spriteWidth,
                    (int) yCoor + this.spriteHeight);

            canvas.drawBitmap(play.getScene().getQuestionViewBitmap(this.question.getComplejidad()),
                    origen, destino, paint);
        }
        //-----------------------------------------------------------------------------------------
    }
    public boolean isFinished(){
        return spriteIndex >=this.scene.getQuestionViewImgNumber();
    }

    public Question getQuestion() {
        return question;
    }
}
