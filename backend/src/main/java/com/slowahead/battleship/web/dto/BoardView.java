package com.slowahead.battleship.web.dto;

import java.util.List;

import com.slowahead.battleship.game.domain.CellState;

public class BoardView {

    private final int size;
    private final List<List<CellState>> cells;
    private final List<ShipView> ships;

    public BoardView(int size, List<List<CellState>> cells, List<ShipView> ships) {
        this.size = size;
        this.cells = cells;
        this.ships = ships;
    }

    public int getSize() {
        return size;
    }

    public List<List<CellState>> getCells() {
        return cells;
    }

    public List<ShipView> getShips() {
        return ships;
    }
}