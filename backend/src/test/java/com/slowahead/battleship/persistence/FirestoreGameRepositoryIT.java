package com.slowahead.battleship.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.cloud.NoCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.slowahead.battleship.ai.ComputerPlayer;
import com.slowahead.battleship.ai.RandomFleetPlacer;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.domain.Ship;
import com.slowahead.battleship.game.service.GameService;
import com.slowahead.battleship.game.service.PlacementService;
import com.slowahead.battleship.game.session.GameSession;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "FIRESTORE_EMULATOR_HOST", matches = ".+")
class FirestoreGameRepositoryIT {

    private Firestore firestore;
    private FirestoreGameRepository repository;

    @BeforeEach
    void setUp() {
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setProjectId("test-project")
                .setHost(System.getenv("FIRESTORE_EMULATOR_HOST"))
                .setCredentials(NoCredentials.getInstance())
                .build();
        firestore = options.getService();
        repository = new FirestoreGameRepository(firestore, new GameDocumentMapper(new PlacementService()));
    }

    @AfterEach
    void tearDown() throws Exception {
        if (firestore != null) {
            firestore.close();
        }
    }

    @Test
    void savesAndLoadsSession() {
        ComputerPlayer computerPlayer = new ComputerPlayer(new Random(11));
        List<Ship> playerShips = new RandomFleetPlacer(new Random(21)).placeFleet();
        Game game = new GameService(new PlacementService())
                .createGame(playerShips, computerPlayer.placeFleet());
        GameSession session = new GameSession("it-game-1", game, computerPlayer);

        repository.save(session);
        GameSession loaded = repository.findById("it-game-1").orElseThrow(AssertionError::new);

        assertThat(loaded.getGame().getPlayerBoard().getShips())
                .containsExactlyInAnyOrderElementsOf(playerShips);
    }
}