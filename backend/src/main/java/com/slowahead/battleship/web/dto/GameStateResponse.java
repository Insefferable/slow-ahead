package com.slowahead.battleship.web.dto;

import com.slowahead.battleship.game.domain.GameStatus;
import com.slowahead.battleship.game.domain.Turn;

public class GameStateResponse {

    private final String gameId;
    private final GameStatus status;
    private final Turn turn;
    private final BoardView playerBoard;
    private final BoardView opponentBoard;

    public GameStateResponse(String gameId, GameStatus status, Turn turn,
                             BoardView playerBoard, BoardView opponentBoard) {
        this.gameId = gameId;
        this.status = status;
        this.turn = turn;
        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;
    }

    public String getGameId() {
        return gameId;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Turn getTurn() {
        return turn;
    }

    public BoardView getPlayerBoard() {
        return playerBoard;
    }

    public BoardView getOpponentBoard() {
        return opponentBoard;
    }
}