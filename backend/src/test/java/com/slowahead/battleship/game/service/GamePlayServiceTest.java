package com.slowahead.battleship.game.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.ai.RandomFleetPlacer;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.GameStatus;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.Turn;
import com.slowahead.battleship.game.exception.GameNotFoundException;
import com.slowahead.battleship.game.exception.InvalidPlacementException;
import com.slowahead.battleship.game.session.GameSession;
import com.slowahead.battleship.game.session.InMemoryGameRepository;
import com.slowahead.battleship.game.session.MoveResult;

class GamePlayServiceTest {

    private final GamePlayService service = new GamePlayService(
            new GameService(new PlacementService()), new InMemoryGameRepository());

    @Test
    void createsGameWithRandomPlayerFleet() {
        GameSession session = service.createGame(null, true);

        assertThat(session.getId()).isNotBlank();
        assertThat(session.getGame().getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(session.getGame().getCurrentTurn()).isEqualTo(Turn.PLAYER);
        assertThat(session.getGame().getPlayerBoard().getShips()).hasSize(5);
    }

    @Test
    void createsGameWithExplicitPlayerFleet() {
        List<Ship> fleet = new RandomFleetPlacer(new Random(7)).placeFleet();

        GameSession session = service.createGame(fleet, false);

        assertThat(session.getGame().getPlayerBoard().getShips()).hasSize(5);
    }

    @Test
    void rejectsExplicitFleetWhenMissing() {
        assertThatThrownBy(() -> service.createGame(null, false))
                .isInstanceOf(InvalidPlacementException.class);
    }

    @Test
    void playerMoveRunsComputerReply() {
        GameSession session = service.createGame(null, true);

        MoveResult result = service.playPlayerMove(session.getId(), new Coordinate(0, 0));

        assertThat(result.getPlayerAttack()).isNotNull();
        assertThat(result.getOpponentAttack()).isPresent();
        assertThat(session.getGame().getPlayerBoard().getShots()).hasSize(1);
        assertThat(session.getMoves()).hasSize(2);
    }

    @Test
    void unknownGameThrows() {
        assertThatThrownBy(() -> service.getGame("missing"))
                .isInstanceOf(GameNotFoundException.class);
    }
}