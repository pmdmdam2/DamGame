package com.example.damgame.model;

import com.example.damgame.activities.GameActivity;
import com.example.damgame.utils.GameUtil;
import com.example.damgame.views.DesertScene;
import com.example.damgame.views.QuestionView;
import com.example.damgame.views.Scene;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Juego, recoge las principales características del juego
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class Play {
    private LocalDateTime starDateTime;
    private LocalDateTime endDateTime;
    private Scene scene;
    private Player player;
    private GameConfig config;
    private int questionsCaptured = 0;
    private int questionsCreated = 0;
    private int points;
    private int lifes;
    private ArrayList<Question> questions = new ArrayList<Question>();
    private ArrayList<QuestionView> questionViews = new ArrayList<QuestionView>();

    private Play() {
    }

    public static Play createGameMove(GameActivity gameActivity, int sceneCode
            , GameConfig gameConfig) {
        Play play = new Play();
        play.setConfig(gameConfig);
        switch (sceneCode) {
            case GameUtil.TEMA_DESIERTO:
                play.setLifes(3);
                play.scene = new DesertScene(gameActivity);
                play.questions = new ArrayList<Question>();
                break;
            case GameUtil.TEMA_ESPACIO:
                break;

            case GameUtil.TEMA_CIUDAD:
                break;

            case GameUtil.TEMA_HIELO:
                break;
            case GameUtil.TEMA_SELVA:
                break;
            case GameUtil.TEMA_SUBMARINO:
                break;
        }
        return play;
    }

    public Scene getScene() {
        return scene;
    }

    public int getQuestionsCaptured() {
        return questionsCaptured;
    }

    public void setQuestionsCaptured(int questionsCaptured) {
        this.questionsCaptured = questionsCaptured;
    }

    public int getQuestionsCreated() {
        return questionsCreated;
    }

    public void setQuestionsCreated(int questionsCreated) {
        this.questionsCreated = questionsCreated;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getStarDateTime() {
        return starDateTime;
    }

    public void setStarDateTime(LocalDateTime starDateTime) {
        this.starDateTime = starDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public GameConfig getConfig() {
        return config;
    }

    private void setConfig(GameConfig config) {
        this.config = config;
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public ArrayList<QuestionView> getQuestionViews() {
        return questionViews;
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public boolean isFinished() {
        if (this.lifes == 0 || (this.scene.getNextImgIndex() < this.scene.getCurrentImgIndex()
                && this.scene.getNextImgIndex() == 0))
            return true;
        return false;
    }
}
