package com.slowahead.battleship.ai;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Fleet;
import com.slowahead.battleship.game.domain.Orientation;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.ShipType;

public class RandomFleetPlacer {

    private static final int FLEET_ATTEMPTS = 50;
    private static final int SHIP_PLACEMENT_ATTEMPTS = 100;

    private final Random random;

    public RandomFleetPlacer(Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    public List<Ship> placeFleet() {
        return placeFleet(Board.DEFAULT_SIZE);
    }

    public List<Ship> placeFleet(int boardSize) {
        for (int attempt = 0; attempt < FLEET_ATTEMPTS; attempt++) {
            Board board = new Board(boardSize);
            if (placeFleet(board)) {
                return Collections.unmodifiableList(board.getShips());
            }
        }
        throw new IllegalStateException("Unable to place fleet after " + FLEET_ATTEMPTS + " attempts");
    }

    private boolean placeFleet(Board board) {
        for (ShipType type : Fleet.standard()) {
            if (!placeShip(board, type)) {
                return false;
            }
        }
        return true;
    }

    private boolean placeShip(Board board, ShipType type) {
        for (int attempt = 0; attempt < SHIP_PLACEMENT_ATTEMPTS; attempt++) {
            Coordinate start = new Coordinate(
                    random.nextInt(board.getSize()), random.nextInt(board.getSize()));
            Orientation orientation = random.nextBoolean()
                    ? Orientation.HORIZONTAL
                    : Orientation.VERTICAL;
            Ship ship = new Ship(type, start, orientation);
            if (board.canPlace(ship)) {
                board.place(ship);
                return true;
            }
        }
        return false;
    }
}