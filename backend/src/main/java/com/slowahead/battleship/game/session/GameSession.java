package com.slowahead.battleship.game.session;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.slowahead.battleship.ai.ComputerPlayer;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.domain.Turn;

public final class GameSession {

    private final String id;
    private final Game game;
    private final ComputerPlayer computerPlayer;
    private final List<Move> moves = new ArrayList<>();
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public GameSession(String id, Game game, ComputerPlayer computerPlayer) {
        this.id = Objects.requireNonNull(id, "id");
        this.game = Objects.requireNonNull(game, "game");
        this.computerPlayer = Objects.requireNonNull(computerPlayer, "computerPlayer");
    }

    public String getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public ComputerPlayer getComputerPlayer() {
        return computerPlayer;
    }

    public List<Move> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void recordMove(Turn side, Coordinate coordinate) {
        moves.add(new Move(side, coordinate));
        updatedAt = Instant.now();
    }
}