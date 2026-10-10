package com.slowahead.battleship.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.CellState;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Orientation;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.ShipType;
import com.slowahead.battleship.web.dto.BoardView;

class GameMapperTest {

    private static Board boardWithDestroyer() {
        Board board = new Board();
        board.place(new Ship(ShipType.DESTROYER, new Coordinate(0, 0), Orientation.HORIZONTAL));
        return board;
    }

    @Test
    void ownBoardRevealsShips() {
        BoardView view = GameMapper.toOwnBoard(boardWithDestroyer());

        assertThat(view.getCells().get(0).get(0)).isEqualTo(CellState.SHIP);
        assertThat(view.getShips()).hasSize(1);
    }

    @Test
    void targetBoardHidesUnhitShips() {
        BoardView view = GameMapper.toTargetBoard(boardWithDestroyer());

        assertThat(view.getCells().get(0).get(0)).isEqualTo(CellState.EMPTY);
        assertThat(view.getShips()).isEmpty();
    }

    @Test
    void targetBoardShowsHits() {
        Board board = boardWithDestroyer();
        board.attack(new Coordinate(0, 0));

        BoardView view = GameMapper.toTargetBoard(board);

        assertThat(view.getCells().get(0).get(0)).isEqualTo(CellState.HIT);
    }
}