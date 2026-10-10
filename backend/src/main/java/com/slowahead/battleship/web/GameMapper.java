package com.slowahead.battleship.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.slowahead.battleship.game.domain.AttackResult;
import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.CellState;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.session.GameSession;
import com.slowahead.battleship.game.session.MoveResult;
import com.slowahead.battleship.web.dto.AttackView;
import com.slowahead.battleship.web.dto.BoardView;
import com.slowahead.battleship.web.dto.GameStateResponse;
import com.slowahead.battleship.web.dto.MoveResultResponse;
import com.slowahead.battleship.web.dto.ShipView;

public final class GameMapper {

    private GameMapper() {
    }

    public static GameStateResponse toState(GameSession session) {
        Game game = session.getGame();
        return new GameStateResponse(
                session.getId(),
                game.getStatus(),
                game.getCurrentTurn(),
                toOwnBoard(game.getPlayerBoard()),
                toTargetBoard(game.getOpponentBoard()));
    }

    public static MoveResultResponse toMoveResult(MoveResult result) {
        return new MoveResultResponse(
                toAttack(result.getPlayerAttack()),
                result.getOpponentAttack().map(GameMapper::toAttack).orElse(null),
                toState(result.getSession()));
    }

    static BoardView toOwnBoard(Board board) {
        return new BoardView(board.getSize(), cells(board, false), ships(board));
    }

    static BoardView toTargetBoard(Board board) {
        return new BoardView(board.getSize(), cells(board, true), Collections.emptyList());
    }

    private static List<List<CellState>> cells(Board board, boolean hideShips) {
        List<List<CellState>> rows = new ArrayList<>();
        for (int row = 0; row < board.getSize(); row++) {
            List<CellState> rowCells = new ArrayList<>();
            for (int col = 0; col < board.getSize(); col++) {
                CellState state = board.cellState(new Coordinate(row, col));
                if (hideShips && state == CellState.SHIP) {
                    state = CellState.EMPTY;
                }
                rowCells.add(state);
            }
            rows.add(rowCells);
        }
        return rows;
    }

    private static List<ShipView> ships(Board board) {
        List<ShipView> views = new ArrayList<>();
        for (Ship ship : board.getShips()) {
            views.add(new ShipView(
                    ship.getType(),
                    ship.getStart().getRow(),
                    ship.getStart().getCol(),
                    ship.getOrientation(),
                    ship.isSunk()));
        }
        return views;
    }

    private static AttackView toAttack(AttackResult result) {
        return new AttackView(
                result.getCoordinate().getRow(),
                result.getCoordinate().getCol(),
                result.getOutcome(),
                result.getSunkShipType().orElse(null));
    }
}