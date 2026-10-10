package com.slowahead.battleship.game.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.slowahead.battleship.game.exception.InvalidPlacementException;

public final class Board {

    public static final int DEFAULT_SIZE = 10;

    private final int size;
    private final List<Ship> ships = new ArrayList<>();
    private final Map<Coordinate, Ship> occupancy = new HashMap<>();

    public Board() {
        this(DEFAULT_SIZE);
    }

    public Board(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public List<Ship> getShips() {
        return Collections.unmodifiableList(ships);
    }

    public boolean isWithinBounds(Coordinate coordinate) {
        return coordinate.getRow() >= 0 && coordinate.getRow() < size
                && coordinate.getCol() >= 0 && coordinate.getCol() < size;
    }

    public boolean canPlace(Ship ship) {
        for (Coordinate coordinate : ship.getOccupiedCoordinates()) {
            if (!isWithinBounds(coordinate) || occupancy.containsKey(coordinate)) {
                return false;
            }
        }
        return true;
    }

    public void place(Ship ship) {
        if (!canPlace(ship)) {
            throw new InvalidPlacementException("Cannot place " + ship + ": out of bounds or overlapping");
        }
        ships.add(ship);
        for (Coordinate coordinate : ship.getOccupiedCoordinates()) {
            occupancy.put(coordinate, ship);
        }
    }

    public Optional<Ship> shipAt(Coordinate coordinate) {
        return Optional.ofNullable(occupancy.get(coordinate));
    }

    public CellState cellState(Coordinate coordinate) {
        return occupancy.containsKey(coordinate) ? CellState.SHIP : CellState.EMPTY;
    }
}