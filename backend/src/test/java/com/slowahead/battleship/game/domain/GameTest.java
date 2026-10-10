package com.slowahead.battleship.game.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.game.exception.GameOverException;

class GameTest {

    private static Board fleetBoard() {
        Board board = new Board();
        board.place(new Ship(ShipType.CARRIER, new Coordinate(0, 0), Orientation.HORIZONTAL));
        board.place(new Ship(ShipType.BATTLESHIP, new Coordinate(2, 0), Orientation.HORIZONTAL));
        board.place(new Ship(ShipType.CRUISER, new Coordinate(4, 0), Orientation.HORIZONTAL));
        board.place(new Ship(ShipType.SUBMARINE, new Coordinate(6, 0), Orientation.HORIZONTAL));
        board.place(new Ship(ShipType.DESTROYER, new Coordinate(8, 0), Orientation.HORIZONTAL));
        return board;
    }

    private static List<Coordinate> opponentFleetCells() {
        return Arrays.asList(
                new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3), new Coordinate(0, 4),
                new Coordinate(2, 0), new Coordinate(2, 1), new Coordinate(2, 2), new Coordinate(2, 3),
                new Coordinate(4, 0), new Coordinate(4, 1), new Coordinate(4, 2),
                new Coordinate(6, 0), new Coordinate(6, 1), new Coordinate(6, 2),
                new Coordinate(8, 0), new Coordinate(8, 1));
    }

    private static Coordinate opponentShot(int index) {
        return new Coordinate((index / 10) * 2 + 1, index % 10);
    }

    private static void winAsPlayer(Game game) {
        int opponentShots = 0;
        for (Coordinate coordinate : opponentFleetCells()) {
            game.attack(coordinate);
            if (!game.isOver()) {
                game.attack(opponentShot(opponentShots++));
            }
        }
    }

    @Test
    void startsOnPlayerTurn() {
        Game game = new Game(fleetBoard(), fleetBoard());

        assertThat(game.getCurrentTurn()).isEqualTo(Turn.PLAYER);
        assertThat(game.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(game.isOver()).isFalse();
    }

    @Test
    void alternatesTurnsAfterEachAttack() {
        Game game = new Game(fleetBoard(), fleetBoard());

        game.attack(new Coordinate(9, 9));
        assertThat(game.getCurrentTurn()).isEqualTo(Turn.OPPONENT);

        game.attack(new Coordinate(9, 9));
        assertThat(game.getCurrentTurn()).isEqualTo(Turn.PLAYER);
    }

    @Test
    void playerWinsBySinkingEntireOpponentFleet() {
        Game game = new Game(fleetBoard(), fleetBoard());

        winAsPlayer(game);

        assertThat(game.getStatus()).isEqualTo(GameStatus.PLAYER_WON);
        assertThat(game.isOver()).isTrue();
    }

    @Test
    void rejectsAttackAfterGameOver() {
        Game game = new Game(fleetBoard(), fleetBoard());
        winAsPlayer(game);

        assertThatThrownBy(() -> game.attack(new Coordinate(9, 9)))
                .isInstanceOf(GameOverException.class);
    }
}