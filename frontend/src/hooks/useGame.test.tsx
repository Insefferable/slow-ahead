import { act, renderHook } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import { useGame } from './useGame'
import type { GameState } from '../api/types'

vi.mock('../api/games', () => ({
  createGame: vi.fn(),
  getGame: vi.fn(),
  playMove: vi.fn(),
  deleteGame: vi.fn(),
}))

import { createGame, playMove } from '../api/games'

const state: GameState = {
  gameId: 'game-1',
  status: 'IN_PROGRESS',
  turn: 'PLAYER',
  playerBoard: { size: 10, cells: [], ships: [] },
  opponentBoard: { size: 10, cells: [], ships: [] },
}

describe('useGame', () => {
  it('starts a random-fleet game', async () => {
    vi.mocked(createGame).mockResolvedValue(state)
    const { result } = renderHook(() => useGame())

    await act(async () => {
      await result.current.startWithRandomFleet()
    })

    expect(result.current.game?.gameId).toBe('game-1')
  })

  it('applies the move result on fire', async () => {
    vi.mocked(createGame).mockResolvedValue(state)
    vi.mocked(playMove).mockResolvedValue({
      playerAttack: { row: 0, col: 0, outcome: 'HIT', sunkShipType: null },
      opponentAttack: null,
      state: { ...state, turn: 'OPPONENT' },
    })
    const { result } = renderHook(() => useGame())

    await act(async () => {
      await result.current.startWithRandomFleet()
    })
    await act(async () => {
      await result.current.fire(0, 0)
    })

    expect(playMove).toHaveBeenCalledWith('game-1', 0, 0)
    expect(result.current.lastAttacks?.player.outcome).toBe('HIT')
  })
})