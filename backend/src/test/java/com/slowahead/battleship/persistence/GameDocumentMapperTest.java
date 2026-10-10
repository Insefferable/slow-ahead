package com.slowahead.battleship.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.ai.ComputerPlayer;
import com.slowahead.battleship.ai.RandomFleetPlacer;
import com.slowahead.battleship.ai.RandomShotStrategy;
import com.slowahead.battleship.game.domain.AttackResult;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.Turn;
import com.slowahead.battleship.game.service.GameService;
import com.slowahead.battleship.game.service.PlacementService;
import com.slowahead.battleship.game.session.GameSession;

class GameDocumentMapperTest {

    private final GameDocumentMapper mapper = new GameDocumentMapper(new PlacementService());
    private final GameService gameService = new GameService(new PlacementService());

    private GameSession playedSession() {
        ComputerPlayer computerPlayer = new ComputerPlayer(new Random(11));
        List<Ship> playerShips = new RandomFleetPlacer(new Random(21)).placeFleet();
        Game game = gameService.createGame(playerShips, computerPlayer.placeFleet());
        GameSession session = new GameSession("game-1", game, computerPlayer);

        RandomShotStrategy playerStrategy = new RandomShotStrategy(new Random(31));
        for (int i = 0; i < 6 && !game.isOver(); i++) {
            Coordinate target = playerStrategy.chooseTarget(game.getOpponentBoard());
            game.attack(target);
            session.recordMove(Turn.PLAYER, target);
            if (!game.isOver()) {
                AttackResult result = computerPlayer.takeTurn(game);
                session.recordMove(Turn.OPPONENT, result.getCoordinate());
            }
        }
        return session;
    }

    @Test
    void documentUsesPrimitiveFriendlyValues() {
        Map<String, Object> document = mapper.toDocument(playedSession());

        assertThat(document.get("playerShips")).isInstanceOf(List.class);
        assertThat(document.get("createdAt")).isInstanceOf(Long.class);
    }

    @Test
    void roundTripsFullSessionState() {
        GameSession original = playedSession();

        GameSession restored = mapper.toSession(original.getId(), mapper.toDocument(original));

        assertThat(restored.getId()).isEqualTo(original.getId());
        assertThat(restored.getGame().getStatus()).isEqualTo(original.getGame().getStatus());
        assertThat(restored.getGame().getCurrentTurn()).isEqualTo(original.getGame().getCurrentTurn());
        assertThat(restored.getGame().getPlayerBoard().getShots())
                .isEqualTo(original.getGame().getPlayerBoard().getShots());
        assertThat(restored.getGame().getOpponentBoard().getShots())
                .isEqualTo(original.getGame().getOpponentBoard().getShots());
        assertThat(restored.getGame().getPlayerBoard().getShips())
                .containsExactlyInAnyOrderElementsOf(original.getGame().getPlayerBoard().getShips());
        assertThat(restored.getGame().getOpponentBoard().getShips())
                .containsExactlyInAnyOrderElementsOf(original.getGame().getOpponentBoard().getShips());
        assertThat(restored.getMoves()).hasSameSizeAs(original.getMoves());
    }
}