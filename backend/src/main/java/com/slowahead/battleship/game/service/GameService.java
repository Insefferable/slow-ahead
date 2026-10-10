package com.slowahead.battleship.game.service;

import java.util.List;

import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.domain.Ship;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final PlacementService placementService;

    public GameService(PlacementService placementService) {
        this.placementService = placementService;
    }

    public Game createGame(List<Ship> playerShips, List<Ship> opponentShips) {
        Board playerBoard = placementService.placeFleet(playerShips);
        Board opponentBoard = placementService.placeFleet(opponentShips);
        return new Game(playerBoard, opponentBoard);
    }
}