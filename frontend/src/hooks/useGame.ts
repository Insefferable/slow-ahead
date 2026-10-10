import { useCallback, useState } from 'react'
import { ApiError } from '../api/client'
import { createGame, deleteGame, playMove } from '../api/games'
import type { AttackView, GameState, ShipPlacement } from '../api/types'

export interface LastAttacks {
  player: AttackView
  opponent: AttackView | null
}

export interface UseGameResult {
  game: GameState | null
  lastAttacks: LastAttacks | null
  loading: boolean
  error: string | null
  startWithRandomFleet: () => Promise<void>
  startWithFleet: (ships: ShipPlacement[]) => Promise<void>
  fire: (row: number, col: number) => Promise<void>
  resign: () => Promise<void>
}

export function useGame(): UseGameResult {
  const [game, setGame] = useState<GameState | null>(null)
  const [lastAttacks, setLastAttacks] = useState<LastAttacks | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const guard = useCallback(async (action: () => Promise<void>) => {
    setLoading(true)
    setError(null)
    try {
      await action()
    } catch (cause) {
      setError(cause instanceof ApiError ? cause.message : 'Something went wrong')
    } finally {
      setLoading(false)
    }
  }, [])

  const startWithRandomFleet = useCallback(
    () =>
      guard(async () => {
        const state = await createGame(true)
        setGame(state)
        setLastAttacks(null)
      }),
    [guard],
  )

  const startWithFleet = useCallback(
    (ships: ShipPlacement[]) =>
      guard(async () => {
        const state = await createGame(false, ships)
        setGame(state)
        setLastAttacks(null)
      }),
    [guard],
  )

  const fire = useCallback(
    (row: number, col: number) =>
      guard(async () => {
        if (!game || game.status !== 'IN_PROGRESS' || game.turn !== 'PLAYER') {
          return
        }
        const result = await playMove(game.gameId, row, col)
        setGame(result.state)
        setLastAttacks({ player: result.playerAttack, opponent: result.opponentAttack })
      }),
    [game, guard],
  )

  const resign = useCallback(
    () =>
      guard(async () => {
        if (game) {
          await deleteGame(game.gameId)
        }
        setGame(null)
        setLastAttacks(null)
      }),
    [game, guard],
  )

  return { game, lastAttacks, loading, error, startWithRandomFleet, startWithFleet, fire, resign }
}