package com.slowahead.battleship.ai;

import java.util.List;
import java.util.Random;

import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Ship;
import org.springframework.stereotype.Service;

@Service
public class ComputerPlayer {

    private final RandomFleetPlacer fleetPlacer;
    private final ShotStrategy shotStrategy;

    public ComputerPlayer() {
        this(new Random());
    }

    public ComputerPlayer(Random random) {
        this.fleetPlacer = new RandomFleetPlacer(random);
        this.shotStrategy = new RandomShotStrategy(random);
    }

    public List<Ship> placeFleet() {
        return fleetPlacer.placeFleet();
    }

    public Coordinate nextMove(Board playerBoard) {
        return shotStrategy.chooseTarget(playerBoard);
    }
}