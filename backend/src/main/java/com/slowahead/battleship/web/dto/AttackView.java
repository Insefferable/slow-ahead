package com.slowahead.battleship.web.dto;

import com.slowahead.battleship.game.domain.AttackOutcome;
import com.slowahead.battleship.game.domain.ShipType;

public class AttackView {

    private final int row;
    private final int col;
    private final AttackOutcome outcome;
    private final ShipType sunkShipType;

    public AttackView(int row, int col, AttackOutcome outcome, ShipType sunkShipType) {
        this.row = row;
        this.col = col;
        this.outcome = outcome;
        this.sunkShipType = sunkShipType;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public AttackOutcome getOutcome() {
        return outcome;
    }

    public ShipType getSunkShipType() {
        return sunkShipType;
    }
}