package com.slowahead.battleship.persistence;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.slowahead.battleship.ai.ComputerPlayer;
import com.slowahead.battleship.game.domain.AttackResult;
import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.domain.Orientation;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.domain.ShipType;
import com.slowahead.battleship.game.domain.Turn;
import com.slowahead.battleship.game.service.PlacementService;
import com.slowahead.battleship.game.session.GameSession;
import com.slowahead.battleship.game.session.Move;
import org.springframework.stereotype.Component;

@Component
public class GameDocumentMapper {

    private final PlacementService placementService;

    public GameDocumentMapper(PlacementService placementService) {
        this.placementService = placementService;
    }

    public Map<String, Object> toDocument(GameSession session) {
        Game game = session.getGame();
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("playerShips", shipsToData(game.getPlayerBoard().getShips()));
        document.put("opponentShips", shipsToData(game.getOpponentBoard().getShips()));
        document.put("moves", movesToData(session.getMoves()));
        document.put("createdAt", session.getCreatedAt().toEpochMilli());
        document.put("updatedAt", session.getUpdatedAt().toEpochMilli());
        return document;
    }

    public GameSession toSession(String id, Map<String, Object> document) {
        List<Ship> playerShips = shipsFromData(asList(document.get("playerShips")));
        List<Ship> opponentShips = shipsFromData(asList(document.get("opponentShips")));
        List<Move> moves = movesFromData(asList(document.get("moves")));
        Instant createdAt = Instant.ofEpochMilli(asLong(document.get("createdAt")));
        Instant updatedAt = Instant.ofEpochMilli(asLong(document.get("updatedAt")));

        Board playerBoard = placementService.placeFleet(playerShips);
        Board opponentBoard = placementService.placeFleet(opponentShips);
        Game game = new Game(playerBoard, opponentBoard);

        ComputerPlayer computerPlayer = new ComputerPlayer();
        for (Move move : moves) {
            AttackResult result = game.attack(move.getCoordinate());
            if (move.getSide() == Turn.OPPONENT) {
                computerPlayer.observe(result);
            }
        }
        return GameSession.restore(id, game, computerPlayer, moves, createdAt, updatedAt);
    }

    private static List<Map<String, Object>> shipsToData(List<Ship> ships) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (Ship ship : ships) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("type", ship.getType().name());
            entry.put("row", ship.getStart().getRow());
            entry.put("col", ship.getStart().getCol());
            entry.put("orientation", ship.getOrientation().name());
            data.add(entry);
        }
        return data;
    }

    private static List<Ship> shipsFromData(List<Map<String, Object>> data) {
        List<Ship> ships = new ArrayList<>();
        for (Map<String, Object> entry : data) {
            ships.add(new Ship(
                    ShipType.valueOf((String) entry.get("type")),
                    new Coordinate(asInt(entry.get("row")), asInt(entry.get("col"))),
                    Orientation.valueOf((String) entry.get("orientation"))));
        }
        return ships;
    }

    private static List<Map<String, Object>> movesToData(List<Move> moves) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (Move move : moves) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("side", move.getSide().name());
            entry.put("row", move.getCoordinate().getRow());
            entry.put("col", move.getCoordinate().getCol());
            data.add(entry);
        }
        return data;
    }

    private static List<Move> movesFromData(List<Map<String, Object>> data) {
        List<Move> moves = new ArrayList<>();
        for (Map<String, Object> entry : data) {
            moves.add(new Move(
                    Turn.valueOf((String) entry.get("side")),
                    new Coordinate(asInt(entry.get("row")), asInt(entry.get("col")))));
        }
        return moves;
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> asList(Object value) {
        return (List<Map<String, Object>>) value;
    }

    private static int asInt(Object value) {
        return ((Number) value).intValue();
    }

    private static long asLong(Object value) {
        return ((Number) value).longValue();
    }
}