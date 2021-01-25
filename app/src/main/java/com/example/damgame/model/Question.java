package com.example.damgame.model;

/**
 * Pregunta
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class Question {
    private String enunciado;
    private int complejidad;
    private int tipo;
    private CharSequence[]respuestas;
    private String respuestaCorrecta;
    private int puntos;
    public Question(){

    }
    public Question(String enunciado, int complejidad
        , int tipo, CharSequence[] respuestas,
                    String respuestaCorrecta, int puntos){
        this.enunciado=enunciado;
        this.complejidad = complejidad;
        this.tipo = tipo;
        this.respuestas = respuestas;
        this.respuestaCorrecta = respuestaCorrecta;
        this.puntos = puntos;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public int getComplejidad() {
        return complejidad;
    }

    public void setComplejidad(int complejidad) {
        this.complejidad = complejidad;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public CharSequence[] getRespuestas() {
        return respuestas;
    }

    public int getPuntos() {
        return puntos;
    }
}
