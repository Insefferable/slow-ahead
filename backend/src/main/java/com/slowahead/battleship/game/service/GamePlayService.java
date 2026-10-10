package com.slowahead.battleship.game.service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.slowahead.battleship.ai.ComputerPlayer;
import com.slowahead.battleship.ai.RandomFleetPlacer;
import com.slowahead.battleship.game.domain.AttackResult;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.Turn;
import com.slowahead.battleship.game.exception.GameNotFoundException;
import com.slowahead.battleship.game.exception.InvalidPlacementException;
import com.slowahead.battleship.game.session.GameRepository;
import com.slowahead.battleship.game.session.GameSession;
import com.slowahead.battleship.game.session.MoveResult;
import org.springframework.stereotype.Service;

@Service
public class GamePlayService {

    private final GameService gameService;
    private final GameRepository gameRepository;

    public GamePlayService(GameService gameService, GameRepository gameRepository) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
    }

    public GameSession createGame(List<Ship> playerShips, boolean randomFleet) {
        if (!randomFleet && (playerShips == null || playerShips.isEmpty())) {
            throw new InvalidPlacementException("Ship placements are required unless randomFleet is true");
        }

        List<Ship> resolvedPlayerShips = randomFleet
                ? new RandomFleetPlacer(new Random()).placeFleet()
                : playerShips;

        ComputerPlayer computerPlayer = new ComputerPlayer();
        Game game = gameService.createGame(resolvedPlayerShips, computerPlayer.placeFleet());
        GameSession session = new GameSession(UUID.randomUUID().toString(), game, computerPlayer);
        return gameRepository.save(session);
    }

    public GameSession getGame(String id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("No game found with id " + id));
    }

    public MoveResult playPlayerMove(String id, Coordinate coordinate) {
        GameSession session = getGame(id);
        Game game = session.getGame();

        AttackResult playerAttack = game.attack(coordinate);
        session.recordMove(Turn.PLAYER, coordinate);

        AttackResult opponentAttack = null;
        if (!game.isOver()) {
            opponentAttack = session.getComputerPlayer().takeTurn(game);
            session.recordMove(Turn.OPPONENT, opponentAttack.getCoordinate());
        }

        gameRepository.save(session);
        return new MoveResult(session, playerAttack, opponentAttack);
    }

    public void deleteGame(String id) {
        getGame(id);
        gameRepository.deleteById(id);
    }
}