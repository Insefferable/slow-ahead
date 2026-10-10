package com.slowahead.battleship.ai;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.slowahead.battleship.game.domain.AttackResult;
import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.Turn;
import org.springframework.stereotype.Service;

@Service
public class ComputerPlayer {

    private final RandomFleetPlacer fleetPlacer;
    private final ShotStrategy shotStrategy;

    public ComputerPlayer() {
        this(new Random());
    }

    public ComputerPlayer(Random random) {
        this(random, new HuntTargetStrategy(random));
    }

    public ComputerPlayer(Random random, ShotStrategy shotStrategy) {
        this.fleetPlacer = new RandomFleetPlacer(random);
        this.shotStrategy = Objects.requireNonNull(shotStrategy, "shotStrategy");
    }

    public List<Ship> placeFleet() {
        return fleetPlacer.placeFleet();
    }

    public Coordinate nextMove(Board playerBoard) {
        return shotStrategy.chooseTarget(playerBoard);
    }

    public AttackResult takeTurn(Game game) {
        if (game.getCurrentTurn() != Turn.OPPONENT) {
            throw new IllegalStateException("Computer player can only act on the opponent turn");
        }
        Coordinate target = shotStrategy.chooseTarget(game.getPlayerBoard());
        AttackResult result = game.attack(target);
        shotStrategy.recordResult(result);
        return result;
    }
}