import type { BoardView, CellState } from '../api/types'

interface BoardProps {
  board: BoardView
  label: string
  interactive?: boolean
  disabled?: boolean
  lastShot?: { row: number; col: number } | null
  onFire?: (row: number, col: number) => void
}

const STATE_CLASS: Record<CellState, string> = {
  EMPTY: 'cell',
  SHIP: 'cell cell--ship',
  HIT: 'cell cell--hit',
  MISS: 'cell cell--miss',
}

export function Board({
  board,
  label,
  interactive = false,
  disabled = false,
  lastShot = null,
  onFire,
}: BoardProps) {
  return (
    <section className="board-panel">
      <h2>{label}</h2>
      <div
        className="board"
        role="grid"
        aria-label={label}
        style={{ gridTemplateColumns: `repeat(${board.size}, 2rem)` }}
      >
        {board.cells.map((rowCells, row) =>
          rowCells.map((state, col) => {
            const attacked = state === 'HIT' || state === 'MISS'
            const clickable = interactive && !disabled && !attacked
            const isLast = lastShot?.row === row && lastShot?.col === col
            const className = `${STATE_CLASS[state]}${isLast ? ' cell--last' : ''}`
            const cellLabel = `row ${row + 1} column ${col + 1}`

            if (!interactive) {
              return <div key={`${row}-${col}`} role="gridcell" aria-label={cellLabel} className={className} />
            }
            return (
              <button
                key={`${row}-${col}`}
                type="button"
                role="gridcell"
                aria-label={cellLabel}
                className={className}
                disabled={!clickable}
                onClick={() => clickable && onFire?.(row, col)}
              />
            )
          }),
        )}
      </div>
    </section>
  )
}