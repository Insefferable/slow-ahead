import { useState } from 'react'
import type { Orientation, ShipPlacement, ShipType } from '../api/types'

const FLEET: ShipType[] = ['CARRIER', 'BATTLESHIP', 'CRUISER', 'SUBMARINE', 'DESTROYER']
const LENGTHS: Record<ShipType, number> = {
  CARRIER: 5,
  BATTLESHIP: 4,
  CRUISER: 3,
  SUBMARINE: 3,
  DESTROYER: 2,
}
const BOARD_SIZE = 10

interface Cell {
  row: number
  col: number
}

function occupied(ship: ShipPlacement): Cell[] {
  const cells: Cell[] = []
  for (let i = 0; i < LENGTHS[ship.type]; i++) {
    cells.push(
      ship.orientation === 'HORIZONTAL'
        ? { row: ship.row, col: ship.col + i }
        : { row: ship.row + i, col: ship.col },
    )
  }
  return cells
}

interface SetupPanelProps {
  onRandom: () => void
  onStart: (ships: ShipPlacement[]) => void
  loading: boolean
}

export function SetupPanel({ onRandom, onStart, loading }: SetupPanelProps) {
  const [orientation, setOrientation] = useState<Orientation>('HORIZONTAL')
  const [placed, setPlaced] = useState<ShipPlacement[]>([])

  const nextType = FLEET[placed.length]
  const occupiedKeys = new Set<string>()
  for (const ship of placed) {
    for (const cell of occupied(ship)) {
      occupiedKeys.add(`${cell.row},${cell.col}`)
    }
  }

  const tryPlace = (row: number, col: number) => {
    if (!nextType) {
      return
    }
    const candidate: ShipPlacement = { type: nextType, row, col, orientation }
    const cells = occupied(candidate)
    const inBounds = cells.every(
      (cell) => cell.row >= 0 && cell.row < BOARD_SIZE && cell.col >= 0 && cell.col < BOARD_SIZE,
    )
    const free = cells.every((cell) => !occupiedKeys.has(`${cell.row},${cell.col}`))
    if (inBounds && free) {
      setPlaced([...placed, candidate])
    }
  }

  const complete = placed.length === FLEET.length

  return (
    <section className="setup">
      <p className="setup__hint">
        {complete ? 'Fleet ready.' : `Place your ${nextType} (${LENGTHS[nextType]}).`}
      </p>
      <div className="board" role="grid" aria-label="Place your fleet"
        style={{ gridTemplateColumns: `repeat(${BOARD_SIZE}, 2rem)` }}>
        {Array.from({ length: BOARD_SIZE * BOARD_SIZE }, (_, index) => {
          const row = Math.floor(index / BOARD_SIZE)
          const col = index % BOARD_SIZE
          const isShip = occupiedKeys.has(`${row},${col}`)
          return (
            <button
              key={index}
              type="button"
              role="gridcell"
              aria-label={`row ${row + 1} column ${col + 1}`}
              className={isShip ? 'cell cell--ship' : 'cell'}
              onClick={() => tryPlace(row, col)}
            />
          )
        })}
      </div>
      <div className="setup__controls">
        <button
          type="button"
          onClick={() => setOrientation((current) => (current === 'HORIZONTAL' ? 'VERTICAL' : 'HORIZONTAL'))}
          disabled={complete}
        >
          Rotate ({orientation.toLowerCase()})
        </button>
        <button type="button" onClick={() => setPlaced([])} disabled={placed.length === 0}>
          Clear
        </button>
        <button type="button" onClick={onRandom} disabled={loading}>
          Random fleet
        </button>
        <button type="button" onClick={() => onStart(placed)} disabled={!complete || loading}>
          Start battle
        </button>
      </div>
    </section>
  )
}