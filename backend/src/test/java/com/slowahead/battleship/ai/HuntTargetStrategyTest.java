package com.slowahead.battleship.ai;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.game.domain.AttackOutcome;
import com.slowahead.battleship.game.domain.AttackResult;
import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.ShipType;

class HuntTargetStrategyTest {

    private static AttackResult result(Coordinate coordinate, AttackOutcome outcome, ShipType sunkType) {
        return new AttackResult(coordinate, outcome, sunkType);
    }

    @Test
    void huntsUnattackedCellWhenNoTargets() {
        Board board = new Board();
        HuntTargetStrategy strategy = new HuntTargetStrategy(new Random(1));

        Coordinate target = strategy.chooseTarget(board);

        assertThat(board.isWithinBounds(target)).isTrue();
        assertThat(board.isAttacked(target)).isFalse();
    }

    @Test
    void targetsOrthogonalNeighboursAfterHit() {
        Board board = new Board(3);
        HuntTargetStrategy strategy = new HuntTargetStrategy(new Random(1));
        strategy.recordResult(result(new Coordinate(1, 1), AttackOutcome.HIT, null));

        Coordinate target = strategy.chooseTarget(board);

        assertThat(target).isIn(
                new Coordinate(0, 1), new Coordinate(2, 1),
                new Coordinate(1, 0), new Coordinate(1, 2));
    }

    @Test
    void ignoresOutOfBoundsNeighbours() {
        Board board = new Board(3);
        HuntTargetStrategy strategy = new HuntTargetStrategy(new Random(1));
        strategy.recordResult(result(new Coordinate(0, 0), AttackOutcome.HIT, null));

        Coordinate target = strategy.chooseTarget(board);

        assertThat(target).isIn(new Coordinate(1, 0), new Coordinate(0, 1));
    }

    @Test
    void skipsAlreadyAttackedTargets() {
        Board board = new Board(3);
        HuntTargetStrategy strategy = new HuntTargetStrategy(new Random(1));
        strategy.recordResult(result(new Coordinate(0, 0), AttackOutcome.HIT, null));

        Coordinate first = strategy.chooseTarget(board);
        board.attack(first);
        Coordinate second = strategy.chooseTarget(board);

        assertThat(second).isNotEqualTo(first);
        assertThat(board.isAttacked(second)).isFalse();
    }

    @Test
    void keepsHuntingAfterShipSunk() {
        Board board = new Board();
        HuntTargetStrategy strategy = new HuntTargetStrategy(new Random(1));
        strategy.recordResult(result(new Coordinate(4, 4), AttackOutcome.HIT, null));
        strategy.recordResult(result(new Coordinate(4, 5), AttackOutcome.SUNK, ShipType.DESTROYER));

        Coordinate target = strategy.chooseTarget(board);

        assertThat(board.isWithinBounds(target)).isTrue();
        assertThat(board.isAttacked(target)).isFalse();
    }
}