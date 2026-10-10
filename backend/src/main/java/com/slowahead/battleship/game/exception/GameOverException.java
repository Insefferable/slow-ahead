package com.slowahead.battleship.game.exception;

public class GameOverException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GameOverException(String message) {
        super(message);
    }
}