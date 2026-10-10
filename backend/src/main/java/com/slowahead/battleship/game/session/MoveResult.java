package com.slowahead.battleship.game.session;

import java.util.Objects;
import java.util.Optional;

import com.slowahead.battleship.game.domain.AttackResult;

public final class MoveResult {

    private final GameSession session;
    private final AttackResult playerAttack;
    private final AttackResult opponentAttack;

    public MoveResult(GameSession session, AttackResult playerAttack, AttackResult opponentAttack) {
        this.session = Objects.requireNonNull(session, "session");
        this.playerAttack = Objects.requireNonNull(playerAttack, "playerAttack");
        this.opponentAttack = opponentAttack;
    }

    public GameSession getSession() {
        return session;
    }

    public AttackResult getPlayerAttack() {
        return playerAttack;
    }

    public Optional<AttackResult> getOpponentAttack() {
        return Optional.ofNullable(opponentAttack);
    }
}