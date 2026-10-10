package com.slowahead.battleship.web.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MoveRequest {

    @NotNull
    @Min(0)
    private Integer row;

    @NotNull
    @Min(0)
    private Integer col;

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
}