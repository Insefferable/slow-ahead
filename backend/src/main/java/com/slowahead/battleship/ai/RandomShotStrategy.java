package com.slowahead.battleship.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;

public class RandomShotStrategy implements ShotStrategy {

    private final Random random;

    public RandomShotStrategy(Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public Coordinate chooseTarget(Board board) {
        List<Coordinate> available = new ArrayList<>();
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Coordinate coordinate = new Coordinate(row, col);
                if (!board.isAttacked(coordinate)) {
                    available.add(coordinate);
                }
            }
        }
        if (available.isEmpty()) {
            throw new IllegalStateException("No available targets remain on the board");
        }
        return available.get(random.nextInt(available.size()));
    }
}