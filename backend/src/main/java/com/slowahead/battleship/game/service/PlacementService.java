package com.slowahead.battleship.game.service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Fleet;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.ShipType;
import com.slowahead.battleship.game.exception.InvalidPlacementException;
import org.springframework.stereotype.Service;

@Service
public class PlacementService {

    public Board placeFleet(List<Ship> ships) {
        return placeFleet(Board.DEFAULT_SIZE, ships);
    }

    public Board placeFleet(int boardSize, List<Ship> ships) {
        if (ships == null) {
            throw new InvalidPlacementException("Ship placements must not be null");
        }

        Set<ShipType> seen = EnumSet.noneOf(ShipType.class);
        for (Ship ship : ships) {
            if (ship == null) {
                throw new InvalidPlacementException("Ship placements must not contain null");
            }
            if (!seen.add(ship.getType())) {
                throw new InvalidPlacementException("Duplicate ship type: " + ship.getType());
            }
        }
        if (!seen.equals(Fleet.standardSet())) {
            throw new InvalidPlacementException("Fleet must contain exactly: " + Fleet.standardSet());
        }

        Board board = new Board(boardSize);
        for (Ship ship : ships) {
            board.place(ship);
        }
        return board;
    }
}