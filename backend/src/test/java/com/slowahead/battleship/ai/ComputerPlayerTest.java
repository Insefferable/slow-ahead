package com.slowahead.battleship.ai;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

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

    @Test
    void randomVersusRandomGameReachesAConclusion() {
        GameService gameService = new GameService(new PlacementService());
        ComputerPlayer computerPlayer = new ComputerPlayer(new Random(99));
        RandomFleetPlacer humanPlacer = new RandomFleetPlacer(new Random(100));
        RandomShotStrategy humanStrategy = new RandomShotStrategy(new Random(101));

        List<Ship> playerFleet = humanPlacer.placeFleet();
        List<Ship> opponentFleet = computerPlayer.placeFleet();
        Game game = gameService.createGame(playerFleet, opponentFleet);

        int guard = 0;
        while (!game.isOver() && guard++ < 1000) {
            if (game.getCurrentTurn() == Turn.PLAYER) {
                game.attack(humanStrategy.chooseTarget(game.getOpponentBoard()));
            } else {
                game.attack(computerPlayer.nextMove(game.getPlayerBoard()));
            }
        }

        assertThat(game.isOver()).isTrue();
        assertThat(game.getStatus()).isIn(GameStatus.PLAYER_WON, GameStatus.OPPONENT_WON);
    }

    @Test
    void nextMoveTargetsTheGivenBoard() {
        ComputerPlayer computerPlayer = new ComputerPlayer(new Random(5));
        Board playerBoard = new Board();
        playerBoard.place(new Ship(
                ShipType.DESTROYER, new Coordinate(0, 0), Orientation.HORIZONTAL));

        Coordinate target = computerPlayer.nextMove(playerBoard);

        assertThat(playerBoard.isWithinBounds(target)).isTrue();
        assertThat(playerBoard.isAttacked(target)).isFalse();
    }
}