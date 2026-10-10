package com.slowahead.battleship.game.session;

import java.util.Optional;

public interface GameRepository {

    GameSession save(GameSession session);

    Optional<GameSession> findById(String id);

    void deleteById(String id);
}
