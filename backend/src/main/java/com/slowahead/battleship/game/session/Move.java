package com.slowahead.battleship.game.session;

import java.util.Objects;

import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Turn;

public final class Move {

    private final Turn side;
    private final Coordinate coordinate;

    public Move(Turn side, Coordinate coordinate) {
        this.side = Objects.requireNonNull(side, "side");
        this.coordinate = Objects.requireNonNull(coordinate, "coordinate");
    }

    public Turn getSide() {
        return side;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}