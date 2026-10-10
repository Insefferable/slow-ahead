package com.slowahead.battleship.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Random;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;

class RandomShotStrategyTest {

    @Test
    void returnsInBoundsUnattackedCoordinate() {
        Board board = new Board();
        board.attack(new Coordinate(0, 0));

        RandomShotStrategy strategy = new RandomShotStrategy(new Random(1));
        Coordinate target = strategy.chooseTarget(board);

        assertThat(board.isWithinBounds(target)).isTrue();
        assertThat(board.isAttacked(target)).isFalse();
    }

    @Test
    void neverRepeatsAcrossManyShots() {
        Board board = new Board();
        RandomShotStrategy strategy = new RandomShotStrategy(new Random(7));

        for (int i = 0; i < 100; i++) {
            Coordinate target = strategy.chooseTarget(board);
            assertThat(board.isAttacked(target)).isFalse();
            board.attack(target);
        }
    }

    @Test
    void throwsWhenNoTargetsRemain() {
        Board board = new Board(1);
        board.attack(new Coordinate(0, 0));

        RandomShotStrategy strategy = new RandomShotStrategy(new Random(1));

        assertThatThrownBy(() -> strategy.chooseTarget(board))
                .isInstanceOf(IllegalStateException.class);
    }
}