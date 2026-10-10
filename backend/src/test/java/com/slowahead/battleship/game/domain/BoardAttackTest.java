package com.slowahead.battleship.game.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.game.exception.InvalidAttackException;

class BoardAttackTest {

    @Test
    void reportsMissOnEmptyCell() {
        Board board = new Board();
        board.place(new Ship(ShipType.CARRIER, new Coordinate(0, 0), Orientation.HORIZONTAL));

        AttackResult result = board.attack(new Coordinate(9, 9));

        assertThat(result.getOutcome()).isEqualTo(AttackOutcome.MISS);
        assertThat(result.getSunkShipType()).isEmpty();
        assertThat(board.cellState(new Coordinate(9, 9))).isEqualTo(CellState.MISS);
    }

    @Test
    void reportsHitOnShip() {
        Board board = new Board();
        board.place(new Ship(ShipType.CARRIER, new Coordinate(0, 0), Orientation.HORIZONTAL));

        AttackResult result = board.attack(new Coordinate(0, 0));

        assertThat(result.getOutcome()).isEqualTo(AttackOutcome.HIT);
        assertThat(board.cellState(new Coordinate(0, 0))).isEqualTo(CellState.HIT);
    }

    @Test
    void reportsSunkWhenAllShipCellsHit() {
        Board board = new Board();
        board.place(new Ship(ShipType.DESTROYER, new Coordinate(0, 0), Orientation.HORIZONTAL));

        board.attack(new Coordinate(0, 0));
        AttackResult result = board.attack(new Coordinate(0, 1));

        assertThat(result.getOutcome()).isEqualTo(AttackOutcome.SUNK);
        assertThat(result.getSunkShipType()).contains(ShipType.DESTROYER);
        assertThat(board.allShipsSunk()).isTrue();
    }

    @Test
    void rejectsAttackOutsideBoard() {
        Board board = new Board();
        assertThatThrownBy(() -> board.attack(new Coordinate(10, 10)))
                .isInstanceOf(InvalidAttackException.class);
    }

    @Test
    void rejectsRepeatedAttack() {
        Board board = new Board();
        board.attack(new Coordinate(5, 5));
        assertThatThrownBy(() -> board.attack(new Coordinate(5, 5)))
                .isInstanceOf(InvalidAttackException.class);
    }
}