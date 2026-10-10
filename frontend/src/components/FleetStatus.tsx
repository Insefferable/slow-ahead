import type { BoardView } from '../api/types'

export function FleetStatus({ board }: { board: BoardView }) {
  const sunk = board.ships.filter((ship) => ship.sunk).length
  return (
    <section className="fleet">
      <h2>
        Your fleet ({sunk}/{board.ships.length} sunk)
      </h2>
      <ul className="fleet__list">
        {board.ships.map((ship) => (
          <li
            key={`${ship.type}-${ship.row}-${ship.col}`}
            className={ship.sunk ? 'fleet__ship fleet__ship--sunk' : 'fleet__ship'}
          >
            {ship.type} {ship.type === 'CARRIER' ? '■' : ''}
          </li>
        ))}
      </ul>
    </section>
  )
}