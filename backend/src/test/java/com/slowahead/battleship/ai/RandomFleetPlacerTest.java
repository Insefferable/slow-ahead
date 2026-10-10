package com.slowahead.battleship.ai;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Fleet;
import com.slowahead.battleship.game.domain.Ship;

class RandomFleetPlacerTest {

    @Test
    void placesStandardFleetWithinBoundsAndWithoutOverlap() {
        RandomFleetPlacer placer = new RandomFleetPlacer(new Random(42));

        List<Ship> fleet = placer.placeFleet();

        assertThat(fleet).hasSize(Fleet.standard().size());
        assertThat(fleet).extracting(Ship::getType)
                .containsExactlyInAnyOrderElementsOf(Fleet.standard());

        Set<Coordinate> occupied = new HashSet<>();
        for (Ship ship : fleet) {
            for (Coordinate coordinate : ship.getOccupiedCoordinates()) {
                assertThat(occupied.add(coordinate)).as("no overlap at %s", coordinate).isTrue();
            }
        }
    }

    @Test
    void succeedsForManySeeds() {
        for (int seed = 0; seed < 200; seed++) {
            List<Ship> fleet = new RandomFleetPlacer(new Random(seed)).placeFleet();
            assertThat(fleet).hasSize(5);
        }
    }
}