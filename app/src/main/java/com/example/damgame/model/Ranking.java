package com.example.damgame.model;

import java.time.LocalDateTime;

public class Ranking {
    private Player player;
    private long score;
    private int award;
    private LocalDateTime timestamp;
    public Ranking(Player player, long score, int award, LocalDateTime timestamp) {
        this.player = player;
        this.score = score;
        this.award = award;
        this.timestamp = timestamp;
    }

    public Player getPlayer() {
        return player;
    }

    public long getScore() {
        return score;
    }

    public int getAward() {
        return award;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
