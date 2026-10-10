package com.slowahead.battleship.ai;

import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;

public interface ShotStrategy {

    Coordinate chooseTarget(Board board);
}