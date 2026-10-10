package com.slowahead.battleship.game.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ShipTest {

    @Test
    void computesHorizontalOccupiedCoordinates() {
        Ship ship = new Ship(ShipType.CRUISER, new Coordinate(2, 3), Orientation.HORIZONTAL);

        assertThat(ship.getOccupiedCoordinates()).containsExactly(
                new Coordinate(2, 3), new Coordinate(2, 4), new Coordinate(2, 5));
    }

    @Test
    void computesVerticalOccupiedCoordinates() {
        Ship ship = new Ship(ShipType.DESTROYER, new Coordinate(0, 0), Orientation.VERTICAL);

        assertThat(ship.getOccupiedCoordinates()).containsExactly(
                new Coordinate(0, 0), new Coordinate(1, 0));
    }

    @Test
    void becomesSunkWhenAllCellsAreHit() {
        Ship ship = new Ship(ShipType.DESTROYER, new Coordinate(0, 0), Orientation.HORIZONTAL);

        ship.registerHit(new Coordinate(0, 0));
        assertThat(ship.isSunk()).isFalse();

        ship.registerHit(new Coordinate(0, 1));
        assertThat(ship.isSunk()).isTrue();
    }

    @Test
    void rejectsHitOutsideShip() {
        Ship ship = new Ship(ShipType.CARRIER, new Coordinate(0, 0), Orientation.HORIZONTAL);

        assertThatThrownBy(() -> ship.registerHit(new Coordinate(5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}