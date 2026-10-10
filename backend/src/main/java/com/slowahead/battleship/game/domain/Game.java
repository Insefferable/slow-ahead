package com.slowahead.battleship.game.domain;

import java.util.Objects;

import com.slowahead.battleship.game.exception.GameOverException;

public final class Game {

    private final Board playerBoard;
    private final Board opponentBoard;
    private Turn currentTurn = Turn.PLAYER;
    private GameStatus status = GameStatus.IN_PROGRESS;

    public Game(Board playerBoard, Board opponentBoard) {
        this.playerBoard = Objects.requireNonNull(playerBoard, "playerBoard");
        this.opponentBoard = Objects.requireNonNull(opponentBoard, "opponentBoard");
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public Board getOpponentBoard() {
        return opponentBoard;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public GameStatus getStatus() {
        return status;
    }

    public boolean isOver() {
        return status != GameStatus.IN_PROGRESS;
    }

    public AttackResult attack(Coordinate coordinate) {
        if (isOver()) {
            throw new GameOverException("Game is already over: " + status);
        }

        Board target = currentTurn == Turn.PLAYER ? opponentBoard : playerBoard;
        AttackResult result = target.attack(coordinate);

        if (target.allShipsSunk()) {
            status = currentTurn == Turn.PLAYER ? GameStatus.PLAYER_WON : GameStatus.OPPONENT_WON;
        } else {
            currentTurn = currentTurn.other();
        }
        return result;
    }
}