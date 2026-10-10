package com.slowahead.battleship.game.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CoordinateTest {

    @Test
    void equalWhenRowAndColMatch() {
        assertThat(new Coordinate(1, 2)).isEqualTo(new Coordinate(1, 2));
        assertThat(new Coordinate(1, 2).hashCode()).isEqualTo(new Coordinate(1, 2).hashCode());
    }

    @Test
    void notEqualWhenDifferent() {
        assertThat(new Coordinate(1, 2)).isNotEqualTo(new Coordinate(2, 1));
    }
}