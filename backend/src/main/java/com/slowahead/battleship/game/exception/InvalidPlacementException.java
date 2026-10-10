package com.slowahead.battleship.game.exception;

public class InvalidPlacementException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidPlacementException(String message) {
        super(message);
    }
}