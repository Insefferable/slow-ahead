package com.slowahead.battleship.persistence;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.slowahead.battleship.game.exception.PersistenceException;
import com.slowahead.battleship.game.session.GameRepository;
import com.slowahead.battleship.game.session.GameSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FirestoreGameRepository implements GameRepository {

    static final String COLLECTION = "games";

    private final Firestore firestore;
    private final GameDocumentMapper mapper;

    public FirestoreGameRepository(Firestore firestore, GameDocumentMapper mapper) {
        this.firestore = firestore;
        this.mapper = mapper;
    }

    @Override
    public GameSession save(GameSession session) {
        try {
            firestore.collection(COLLECTION)
                    .document(session.getId())
                    .set(mapper.toDocument(session))
                    .get();
            return session;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new PersistenceException("Interrupted while saving game " + session.getId(), exception);
        } catch (ExecutionException exception) {
            throw new PersistenceException("Failed to save game " + session.getId(), exception);
        }
    }

    @Override
    public Optional<GameSession> findById(String id) {
        try {
            DocumentSnapshot snapshot = firestore.collection(COLLECTION).document(id).get().get();
            if (!snapshot.exists()) {
                return Optional.empty();
            }
            return Optional.of(mapper.toSession(snapshot.getId(), snapshot.getData()));
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new PersistenceException("Interrupted while loading game " + id, exception);
        } catch (ExecutionException exception) {
            throw new PersistenceException("Failed to load game " + id, exception);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            firestore.collection(COLLECTION).document(id).delete().get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new PersistenceException("Interrupted while deleting game " + id, exception);
        } catch (ExecutionException exception) {
            throw new PersistenceException("Failed to delete game " + id, exception);
        }
    }
}