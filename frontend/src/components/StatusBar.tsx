import type { AttackView, GameState } from '../api/types'
import type { LastAttacks } from '../hooks/useGame'

function describe(attack: AttackView | null, subject: string): string | null {
  if (!attack) {
    return null
  }
  switch (attack.outcome) {
    case 'MISS':
      return `${subject} missed at ${attack.row + 1},${attack.col + 1}.`
    case 'HIT':
      return `${subject} hit a ship at ${attack.row + 1},${attack.col + 1}!`
    case 'SUNK':
      return `${subject} sank the ${attack.sunkShipType?.toLowerCase() ?? 'ship'}!`
  }
}

interface StatusBarProps {
  game: GameState
  lastAttacks: LastAttacks | null
  loading: boolean
}

export function StatusBar({ game, lastAttacks, loading }: StatusBarProps) {
  if (game.status === 'PLAYER_WON') {
    return <p className="status status--win">You win — the enemy fleet is destroyed.</p>
  }
  if (game.status === 'OPPONENT_WON') {
    return <p className="status status--lose">You lose — your fleet has been sunk.</p>
  }

  const playerLine = lastAttacks ? describe(lastAttacks.player, 'You') : null
  const opponentLine = lastAttacks ? describe(lastAttacks.opponent, 'Enemy') : null

  return (
    <div className="status">
      <p className="status__turn">
        {loading ? '…' : game.turn === 'PLAYER' ? 'Your turn — fire at enemy waters.' : 'Enemy is firing…'}
      </p>
      {(playerLine || opponentLine) && (
        <ul className="status__log">
          {playerLine && <li>{playerLine}</li>}
          {opponentLine && <li>{opponentLine}</li>}
        </ul>
      )}
    </div>
  )
}