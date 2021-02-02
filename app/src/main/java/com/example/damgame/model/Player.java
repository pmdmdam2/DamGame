package com.example.damgame.model;

/**
 * Jugador
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class Player {
    private String nick;
    private String password;
    private String email;

    public Player(String nick, String password, String email) {
        this.nick = nick;
        this.password = password;
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
