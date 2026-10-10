import { afterEach, describe, expect, it, vi } from 'vitest'
import { ApiError } from './client'
import { createGame, playMove } from './games'

function jsonResponse(status: number, body: unknown): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { 'Content-Type': 'application/json' },
  })
}

describe('games api', () => {
  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('creates a random-fleet game', async () => {
    const state = {
      gameId: '1',
      status: 'IN_PROGRESS',
      turn: 'PLAYER',
      playerBoard: { size: 10, cells: [], ships: [] },
      opponentBoard: { size: 10, cells: [], ships: [] },
    }
    const fetchMock = vi.fn().mockResolvedValue(jsonResponse(201, state))
    vi.stubGlobal('fetch', fetchMock)

    const result = await createGame(true)

    expect(result.gameId).toBe('1')
    expect(fetchMock).toHaveBeenCalledWith('/api/games', expect.objectContaining({ method: 'POST' }))
  })

  it('throws ApiError carrying the backend message', async () => {
    const fetchMock = vi.fn().mockResolvedValue(jsonResponse(400, { message: 'Coordinate already attacked' }))
    vi.stubGlobal('fetch', fetchMock)

    await expect(playMove('1', 0, 0)).rejects.toBeInstanceOf(ApiError)
  })
})