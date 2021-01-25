package com.example.damgame.model;

/**
 * Gestiona la configuración de la aplicación
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class GameConfig {
    private int questions=500;
    private int minutesToQuestion=50;
    private int framesToNewQuestion=0;
    private int gravity=3;

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getMinutesToQuestion() {
        return minutesToQuestion;
    }

    public void setMinutesToQuestion(int minutesToQuestion) {
        this.minutesToQuestion = minutesToQuestion;
    }

    public int getFramesToNewQuestion() {
        return framesToNewQuestion;
    }

    public void setFramesToNewQuestion(int framesToNewQuestion) {
        this.framesToNewQuestion = framesToNewQuestion;
    }

    public int getGravity(){
        return this.gravity;
    }
}
