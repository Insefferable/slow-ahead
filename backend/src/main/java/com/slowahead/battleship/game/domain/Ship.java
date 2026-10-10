package com.slowahead.battleship.game.domain;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class Ship {

    private final ShipType type;
    private final Orientation orientation;
    private final Coordinate start;
    private final Set<Coordinate> hits = new LinkedHashSet<>();

    public Ship(ShipType type, Coordinate start, Orientation orientation) {
        this.type = Objects.requireNonNull(type, "type");
        this.start = Objects.requireNonNull(start, "start");
        this.orientation = Objects.requireNonNull(orientation, "orientation");
    }

    public ShipType getType() {
        return type;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Coordinate getStart() {
        return start;
    }

    public int getLength() {
        return type.getLength();
    }

    public Set<Coordinate> getOccupiedCoordinates() {
        Set<Coordinate> occupied = new LinkedHashSet<>();
        for (int offset = 0; offset < getLength(); offset++) {
            occupied.add(cellAt(offset));
        }
        return Collections.unmodifiableSet(occupied);
    }

    public boolean occupies(Coordinate coordinate) {
        return getOccupiedCoordinates().contains(coordinate);
    }

    public void registerHit(Coordinate coordinate) {
        if (!occupies(coordinate)) {
            throw new IllegalArgumentException("Coordinate " + coordinate + " is not part of this ship");
        }
        hits.add(coordinate);
    }

    public boolean isHitAt(Coordinate coordinate) {
        return hits.contains(coordinate);
    }

    public boolean isSunk() {
        return hits.size() == getLength();
    }

    private Coordinate cellAt(int offset) {
        if (orientation == Orientation.HORIZONTAL) {
            return new Coordinate(start.getRow(), start.getCol() + offset);
        }
        return new Coordinate(start.getRow() + offset, start.getCol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ship)) {
            return false;
        }
        Ship ship = (Ship) o;
        return type == ship.type && orientation == ship.orientation && start.equals(ship.start);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, orientation, start);
    }

    @Override
    public String toString() {
        return "Ship{type=" + type + ", start=" + start + ", orientation=" + orientation + '}';
    }
}