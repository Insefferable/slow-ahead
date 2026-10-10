package com.slowahead.battleship.game.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.domain.Orientation;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.ShipType;
import com.slowahead.battleship.game.domain.Turn;

class GameServiceTest {

    private final GameService gameService = new GameService(new PlacementService());

    private static List<Ship> fleet() {
        return Arrays.asList(
                new Ship(ShipType.CARRIER, new Coordinate(0, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.BATTLESHIP, new Coordinate(2, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.CRUISER, new Coordinate(4, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.SUBMARINE, new Coordinate(6, 0), Orientation.HORIZONTAL),
                new Ship(ShipType.DESTROYER, new Coordinate(8, 0), Orientation.HORIZONTAL));
    }

    @Test
    void createsGameWithBothFleetsPlaced() {
        Game game = gameService.createGame(fleet(), fleet());

        assertThat(game.getPlayerBoard().getShips()).hasSize(5);
        assertThat(game.getOpponentBoard().getShips()).hasSize(5);
        assertThat(game.getCurrentTurn()).isEqualTo(Turn.PLAYER);
    }
}