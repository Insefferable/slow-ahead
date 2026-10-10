package com.slowahead.battleship.game.session;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "false", matchIfMissing = true)
public class InMemoryGameRepository implements GameRepository {

    private final ConcurrentMap<String, GameSession> sessions = new ConcurrentHashMap<>();

    @Override
    public GameSession save(GameSession session) {
        sessions.put(session.getId(), session);
        return session;
    }

    @Override
    public Optional<GameSession> findById(String id) {
        return Optional.ofNullable(sessions.get(id));
    }

    @Override
    public void deleteById(String id) {
        sessions.remove(id);
    }
}