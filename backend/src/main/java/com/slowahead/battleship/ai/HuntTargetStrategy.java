package com.slowahead.battleship.ai;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.slowahead.battleship.game.domain.AttackOutcome;
import com.slowahead.battleship.game.domain.AttackResult;
import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;

public class HuntTargetStrategy implements ShotStrategy {

    private final Random random;
    private final Deque<Coordinate> targets = new ArrayDeque<>();
    private int boardSize = Board.DEFAULT_SIZE;

    public HuntTargetStrategy(Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public Coordinate chooseTarget(Board board) {
        boardSize = board.getSize();

        while (!targets.isEmpty()) {
            Coordinate candidate = targets.pollFirst();
            if (board.isWithinBounds(candidate) && !board.isAttacked(candidate)) {
                return candidate;
            }
        }
        return randomHunt(board);
    }

    @Override
    public void recordResult(AttackResult result) {
        if (result.getOutcome() == AttackOutcome.SUNK) {
            targets.clear();
        } else if (result.getOutcome() == AttackOutcome.HIT) {
            enqueueNeighbors(result.getCoordinate());
        }
    }

    private void enqueueNeighbors(Coordinate coordinate) {
        enqueue(new Coordinate(coordinate.getRow() - 1, coordinate.getCol()));
        enqueue(new Coordinate(coordinate.getRow() + 1, coordinate.getCol()));
        enqueue(new Coordinate(coordinate.getRow(), coordinate.getCol() - 1));
        enqueue(new Coordinate(coordinate.getRow(), coordinate.getCol() + 1));
    }

    private void enqueue(Coordinate coordinate) {
        if (coordinate.getRow() >= 0 && coordinate.getRow() < boardSize
                && coordinate.getCol() >= 0 && coordinate.getCol() < boardSize
                && !targets.contains(coordinate)) {
            targets.addLast(coordinate);
        }
    }

    private Coordinate randomHunt(Board board) {
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