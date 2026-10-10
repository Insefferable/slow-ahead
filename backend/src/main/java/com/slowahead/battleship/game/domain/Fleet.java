package com.slowahead.battleship.game.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class Fleet {

    private static final List<ShipType> STANDARD = Collections.unmodifiableList(Arrays.asList(
            ShipType.CARRIER,
            ShipType.BATTLESHIP,
            ShipType.CRUISER,
            ShipType.SUBMARINE,
            ShipType.DESTROYER));

    private Fleet() {
    }

    public static List<ShipType> standard() {
        return STANDARD;
    }

    public static Set<ShipType> standardSet() {
        return Collections.unmodifiableSet(EnumSet.copyOf(STANDARD));
    }
}