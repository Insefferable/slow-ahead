package com.slowahead.battleship.game.domain;

public enum Turn {
    PLAYER,
    OPPONENT;

    public Turn other() {
        return this == PLAYER ? OPPONENT : PLAYER;
    }
}