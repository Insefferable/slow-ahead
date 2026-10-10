package com.slowahead.battleship.web.dto;

import com.slowahead.battleship.game.domain.Orientation;
import com.slowahead.battleship.game.domain.ShipType;

public class ShipView {

    private final ShipType type;
    private final int row;
    private final int col;
    private final Orientation orientation;
    private final boolean sunk;

    public ShipView(ShipType type, int row, int col, Orientation orientation, boolean sunk) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.orientation = orientation;
        this.sunk = sunk;
    }

    public ShipType getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isSunk() {
        return sunk;
    }
}