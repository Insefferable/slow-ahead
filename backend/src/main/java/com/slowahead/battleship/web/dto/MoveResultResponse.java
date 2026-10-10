package com.slowahead.battleship.web.dto;

public class MoveResultResponse {

    private final AttackView playerAttack;
    private final AttackView opponentAttack;
    private final GameStateResponse state;

    public MoveResultResponse(AttackView playerAttack, AttackView opponentAttack, GameStateResponse state) {
        this.playerAttack = playerAttack;
        this.opponentAttack = opponentAttack;
        this.state = state;
    }

    public AttackView getPlayerAttack() {
        return playerAttack;
    }

    public AttackView getOpponentAttack() {
        return opponentAttack;
    }

    public GameStateResponse getState() {
        return state;
    }
}