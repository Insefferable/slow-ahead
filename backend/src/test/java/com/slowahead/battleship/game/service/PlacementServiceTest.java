package com.slowahead.battleship.game.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Orientation;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.ShipType;
import com.slowahead.battleship.game.exception.InvalidPlacementException;

class PlacementServiceTest {

    private final PlacementService placementService = new PlacementService();

    private static List<Ship> standardFleet() {
        return Arrays.asList(
                new Ship(ShipType.CARRIER, new Coordinate(0, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.BATTLESHIP, new Coordinate(2, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.CRUISER, new Coordinate(4, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.SUBMARINE, new Coordinate(6, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.DESTROYER, new Coordinate(8, 0), Orientation.HORIZONTAL));
    }

    @Test
    void placesStandardFleet() {
        Board board = placementService.placeFleet(standardFleet());

        assertThat(board.getShips()).hasSize(5);
        assertThat(board.shipAt(new Coordinate(0, 4)).get().getType()).isEqualTo(ShipType.CARRIER);
    }

    @Test
    void rejectsOutOfBoundsShip() {
        List<Ship> fleet = Arrays.asList(
                new Ship(ShipType.CARRIER, new Coordinate(0, 6), Orientation.HORIZONTAL),
                new Ship(ShipType.BATTLESHIP, new Coordinate(2, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.CRUISER, new Coordinate(4, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.SUBMARINE, new Coordinate(6, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.DESTROYER, new Coordinate(8, 0), Orientation.HORIZONTAL));

        assertThatThrownBy(() -> placementService.placeFleet(fleet))
                .isInstanceOf(InvalidPlacementException.class);
    }

    @Test
    void rejectsOverlappingFleet() {
        List<Ship> fleet = Arrays.asList(
                new Ship(ShipType.CARRIER, new Coordinate(0, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.BATTLESHIP, new Coordinate(0, 0), Orientation.VERTICAL),
                new Ship(ShipType.CRUISER, new Coordinate(4, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.SUBMARINE, new Coordinate(6, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.DESTROYER, new Coordinate(8, 0), Orientation.HORIZONTAL));

        assertThatThrownBy(() -> placementService.placeFleet(fleet))
                .isInstanceOf(InvalidPlacementException.class);
    }

    @Test
    void rejectsDuplicateShipType() {
        List<Ship> fleet = Arrays.asList(
                new Ship(ShipType.CARRIER, new Coordinate(0, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.CARRIER, new Coordinate(2, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.CRUISER, new Coordinate(4, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.SUBMARINE, new Coordinate(6, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.DESTROYER, new Coordinate(8, 0), Orientation.HORIZONTAL));

        assertThatThrownBy(() -> placementService.placeFleet(fleet))
                .isInstanceOf(InvalidPlacementException.class);
    }

    @Test
    void rejectsIncompleteFleet() {
        List<Ship> fleet = Arrays.asList(
                new Ship(ShipType.CARRIER, new Coordinate(0, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.DESTROYER, new Coordinate(8, 0), Orientation.HORIZONTAL));

        assertThatThrownBy(() -> placementService.placeFleet(fleet))
                .isInstanceOf(InvalidPlacementException.class);
    }
}