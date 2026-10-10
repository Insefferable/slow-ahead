package com.slowahead.battleship.web.dto;

import javax.validation.constraints.NotNull;

import com.slowahead.battleship.game.domain.Orientation;
import com.slowahead.battleship.game.domain.ShipType;

public class ShipPlacementRequest {

    @NotNull
    private ShipType type;

    @NotNull
    private Integer row;

    @NotNull
    private Integer col;

    @NotNull
    private Orientation orientation;

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}