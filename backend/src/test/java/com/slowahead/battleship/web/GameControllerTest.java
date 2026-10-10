package com.slowahead.battleship.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.slowahead.battleship.ai.ComputerPlayer;
import com.slowahead.battleship.game.domain.AttackOutcome;
import com.slowahead.battleship.game.domain.AttackResult;
import com.slowahead.battleship.game.domain.Board;
import com.slowahead.battleship.game.domain.Coordinate;
import com.slowahead.battleship.game.domain.Game;
import com.slowahead.battleship.game.exception.GameNotFoundException;
import com.slowahead.battleship.game.exception.InvalidPlacementException;
import com.slowahead.battleship.game.service.GamePlayService;
import com.slowahead.battleship.game.session.GameSession;
import com.slowahead.battleship.game.session.MoveResult;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GamePlayService gamePlayService;

    private static GameSession session() {
        return new GameSession("game-1", new Game(new Board(), new Board()), new ComputerPlayer());
    }

    @Test
    void createReturnsGameState() throws Exception {
        given(gamePlayService.createGame(any(), anyBoolean())).willReturn(session());

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"randomFleet\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameId").value("game-1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.turn").value("PLAYER"));
    }

    @Test
    void createRejectsInvalidPlacement() throws Exception {
        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"randomFleet\":false,\"ships\":[{}]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReturnsBadRequestWhenFleetRejectedByService() throws Exception {
        given(gamePlayService.createGame(any(), anyBoolean()))
                .willThrow(new InvalidPlacementException("Fleet must contain exactly five ships"));

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"randomFleet\":false,\"ships\":[]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReturnsNotFoundForUnknownGame() throws Exception {
        given(gamePlayService.getGame("missing")).willThrow(new GameNotFoundException("nope"));

        mockMvc.perform(get("/api/games/missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    void moveReturnsAttackResult() throws Exception {
        MoveResult moveResult = new MoveResult(session(),
                new AttackResult(new Coordinate(0, 0), AttackOutcome.MISS, null), null);
        given(gamePlayService.playPlayerMove(anyString(), any())).willReturn(moveResult);

        mockMvc.perform(post("/api/games/game-1/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"row\":0,\"col\":0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerAttack.outcome").value("MISS"))
                .andExpect(jsonPath("$.state.gameId").value("game-1"));
    }

    @Test
    void moveRejectsOutOfRangeCoordinate() throws Exception {
        mockMvc.perform(post("/api/games/game-1/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"row\":-1,\"col\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/games/game-1"))
                .andExpect(status().isNoContent());
    }
}