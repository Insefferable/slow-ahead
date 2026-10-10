import { request } from './client'
import type { GameState, MoveResult, ShipPlacement } from './types'

export function createGame(randomFleet: boolean, ships?: ShipPlacement[]): Promise<GameState> {
  return request<GameState>('/api/games', {
    method: 'POST',
    body: JSON.stringify(randomFleet ? { randomFleet: true } : { randomFleet: false, ships }),
  })
}

export function getGame(gameId: string): Promise<GameState> {
  return request<GameState>(`/api/games/${gameId}`)
}

export function playMove(gameId: string, row: number, col: number): Promise<MoveResult> {
  return request<MoveResult>(`/api/games/${gameId}/moves`, {
    method: 'POST',
    body: JSON.stringify({ row, col }),
  })
}

export function deleteGame(gameId: string): Promise<void> {
  return request<void>(`/api/games/${gameId}`, { method: 'DELETE' })
}