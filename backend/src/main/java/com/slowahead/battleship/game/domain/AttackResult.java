package com.slowahead.battleship.game.domain;

import java.util.Objects;
import java.util.Optional;

public final class AttackResult {

    private final Coordinate coordinate;
    private final AttackOutcome outcome;
    private final ShipType sunkShipType;

    public AttackResult(Coordinate coordinate, AttackOutcome outcome, ShipType sunkShipType) {
        this.coordinate = Objects.requireNonNull(coordinate, "coordinate");
        this.outcome = Objects.requireNonNull(outcome, "outcome");
        this.sunkShipType = sunkShipType;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public AttackOutcome getOutcome() {
        return outcome;
    }

    public Optional<ShipType> getSunkShipType() {
        return Optional.ofNullable(sunkShipType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttackResult)) {
            return false;
        }
        AttackResult other = (AttackResult) o;
        return coordinate.equals(other.coordinate)
                && outcome == other.outcome
                && sunkShipType == other.sunkShipType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, outcome, sunkShipType);
    }

    @Override
    public String toString() {
        return "AttackResult{coordinate=" + coordinate + ", outcome=" + outcome
                + ", sunkShipType=" + sunkShipType + '}';
    }
}