package com.slowahead.battleship.game.exception;

public class InvalidAttackException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidAttackException(String message) {
        super(message);
    }
}