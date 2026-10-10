package com.slowahead.battleship.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.service.GamePlayService;
import com.slowahead.battleship.game.session.GameSession;
import com.slowahead.battleship.web.dto.CreateGameRequest;
import com.slowahead.battleship.web.dto.GameStateResponse;
import com.slowahead.battleship.web.dto.MoveRequest;
import com.slowahead.battleship.web.dto.MoveResultResponse;
import com.slowahead.battleship.web.dto.ShipPlacementRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GamePlayService gamePlayService;

    public GameController(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameStateResponse create(@Valid @RequestBody CreateGameRequest request) {
        List<Ship> ships = request.isRandomFleet() ? null : toShips(request.getShips());
        GameSession session = gamePlayService.createGame(ships, request.isRandomFleet());
        return GameMapper.toState(session);
    }

    @GetMapping("/{id}")
    public GameStateResponse get(@PathVariable String id) {
        return GameMapper.toState(gamePlayService.getGame(id));
    }

    @PostMapping("/{id}/moves")
    public MoveResultResponse move(@PathVariable String id, @Valid @RequestBody MoveRequest request) {
        Coordinate coordinate = new Coordinate(request.getRow(), request.getCol());
        return GameMapper.toMoveResult(gamePlayService.playPlayerMove(id, coordinate));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        gamePlayService.deleteGame(id);
    }

    private static List<Ship> toShips(List<ShipPlacementRequest> placements) {
        if (placements == null) {
            return null;
        }
        List<Ship> ships = new ArrayList<>();
        for (ShipPlacementRequest placement : placements) {
            ships.add(new Ship(
                    placement.getType(),
                    new Coordinate(placement.getRow(), placement.getCol()),
                    placement.getOrientation()));
        }
        return ships;
    }
}