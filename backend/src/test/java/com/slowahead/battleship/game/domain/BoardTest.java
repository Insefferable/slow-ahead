package com.slowahead.battleship.game.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.game.exception.InvalidPlacementException;

class BoardTest {

    @Test
    void reportsBounds() {
        Board board = new Board();
        assertThat(board.isWithinBounds(new Coordinate(0, 0))).isTrue();
        assertThat(board.isWithinBounds(new Coordinate(9, 9))).isTrue();
        assertThat(board.isWithinBounds(new Coordinate(10, 0))).isFalse();
        assertThat(board.isWithinBounds(new Coordinate(-1, 0))).isFalse();
    }

    @Test
    void rejectsOverlappingPlacement() {
        Board board = new Board();
        board.place(new Ship(ShipType.CARRIER, new Coordinate(0, 0), Orientation.HORIZONTAL));

        Ship overlapping = new Ship(ShipType.DESTROYER, new Coordinate(0, 4), Orientation.VERTICAL);
        assertThat(board.canPlace(overlapping)).isFalse();
        assertThatThrownBy(() -> board.place(overlapping))
                .isInstanceOf(InvalidPlacementException.class);
    }

    @Test
    void exposesShipAndCellState() {
        Board board = new Board();
        Ship ship = new Ship(ShipType.BATTLESHIP, new Coordinate(3, 3), Orientation.VERTICAL);
        board.place(ship);

        assertThat(board.shipAt(new Coordinate(3, 3))).contains(ship);
        assertThat(board.shipAt(new Coordinate(9, 9))).isEmpty();
        assertThat(board.cellState(new Coordinate(3, 3))).isEqualTo(CellState.SHIP);
        assertThat(board.cellState(new Coordinate(9, 9))).isEqualTo(CellState.EMPTY);
    }
}