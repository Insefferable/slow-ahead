package com.slowahead.battleship.web.dto;

import java.util.List;

import javax.validation.Valid;

public class CreateGameRequest {

    private boolean randomFleet;

    @Valid
    private List<ShipPlacementRequest> ships;

    public boolean isRandomFleet() {
        return randomFleet;
    }

    public void setRandomFleet(boolean randomFleet) {
        this.randomFleet = randomFleet;
    }

    public List<ShipPlacementRequest> getShips() {
        return ships;
    }

    public void setShips(List<ShipPlacementRequest> ships) {
        this.ships = ships;
    }
}