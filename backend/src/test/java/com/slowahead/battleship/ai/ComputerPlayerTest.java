package com.slowahead.battleship.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Random;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.game.domain.AttackResult;
import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.domain.GameStatus;
import com.slowahead.battleship.game.domain.Orientation;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.ShipType;
import com.slowahead.battleship.game.domain.Turn;
import com.slowahead.battleship.game.service.GameService;
import com.slowahead.battleship.game.service.PlacementService;

class ComputerPlayerTest {

    private final GameService gameService = new GameService(new PlacementService());

    @Test
    void huntVersusRandomGameReachesAConclusion() {
        ComputerPlayer computerPlayer = new ComputerPlayer(new Random(99));
        Game game = gameService.createGame(
                new RandomFleetPlacer(new Random(100)).placeFleet(),
                computerPlayer.placeFleet());
        RandomShotStrategy humanStrategy = new RandomShotStrategy(new Random(101));

        int guard = 0;
        while (!game.isOver() && guard++ < 1000) {
            if (game.getCurrentTurn() == Turn.PLAYER) {
                game.attack(humanStrategy.chooseTarget(game.getOpponentBoard()));
            } else {
                computerPlayer.takeTurn(game);
            }
        }

        assertThat(game.isOver()).isTrue();
        assertThat(game.getStatus()).isIn(GameStatus.PLAYER_WON, GameStatus.OPPONENT_WON);
    }

    @Test
    void takeTurnAttacksThePlayerBoardOnOpponentTurn() {
        ComputerPlayer computerPlayer = new ComputerPlayer(new Random(1));
        Game game = gameService.createGame(
                new RandomFleetPlacer(new Random(2)).placeFleet(),
                computerPlayer.placeFleet());

        game.attack(new Coordinate(0, 0));
        assertThat(game.getCurrentTurn()).isEqualTo(Turn.OPPONENT);

        AttackResult result = computerPlayer.takeTurn(game);

        assertThat(result).isNotNull();
        assertThat(game.getPlayerBoard().getShots()).hasSize(1);
    }

    @Test
    void takeTurnRejectsWhenNotOpponentTurn() {
        ComputerPlayer computerPlayer = new ComputerPlayer(new Random(1));
        Game game = gameService.createGame(
                new RandomFleetPlacer(new Random(2)).placeFleet(),
                computerPlayer.placeFleet());

        assertThatThrownBy(() -> computerPlayer.takeTurn(game))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void nextMoveTargetsTheGivenBoard() {
        ComputerPlayer computerPlayer = new ComputerPlayer(new Random(5));
        Board playerBoard = new Board();
        playerBoard.place(new Ship(ShipType.DESTROYER, new Coordinate(0, 0), Orientation.HORIZONTAL));

        Coordinate target = computerPlayer.nextMove(playerBoard);

        assertThat(playerBoard.isWithinBounds(target)).isTrue();
        assertThat(playerBoard.isAttacked(target)).isFalse();
    }
}