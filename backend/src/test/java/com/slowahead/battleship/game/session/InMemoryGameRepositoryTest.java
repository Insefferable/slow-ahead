package com.slowahead.battleship.game.session;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.slowahead.battleship.ai.ComputerPlayer;
import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Game;

class InMemoryGameRepositoryTest {

    private final InMemoryGameRepository repository = new InMemoryGameRepository();

    private static GameSession session(String id) {
        return new GameSession(id, new Game(new Board(), new Board()), new ComputerPlayer());
    }

    @Test
    void savesAndFindsSession() {
        GameSession session = session("id-1");

        repository.save(session);

        assertThat(repository.findById("id-1")).contains(session);
    }

    @Test
    void returnsEmptyForUnknownId() {
        assertThat(repository.findById("missing")).isEmpty();
    }

    @Test
    void deletesSession() {
        repository.save(session("id-2"));

        repository.deleteById("id-2");

        assertThat(repository.findById("id-2")).isEmpty();
    }
}