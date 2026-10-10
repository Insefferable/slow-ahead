import { Board } from './components/Board'
import { FleetStatus } from './components/FleetStatus'
import { SetupPanel } from './components/SetupPanel'
import { StatusBar } from './components/StatusBar'
import { useGame } from './hooks/useGame'

function App() {
  const {
    game,
    lastAttacks,
    loading,
    error,
    startWithRandomFleet,
    startWithFleet,
    fire,
    resign,
  } = useGame()

  return (
    <main className="app">
      <header className="app__header">
        <h1>Battleship</h1>
        {game && (
          <button type="button" onClick={resign} disabled={loading}>
            New game
          </button>
        )}
      </header>

      {error && (
        <p className="error" role="alert">
          {error}
        </p>
      )}

      {!game ? (
        <SetupPanel onRandom={startWithRandomFleet} onStart={startWithFleet} loading={loading} />
      ) : (
        <div className="game">
          <StatusBar game={game} lastAttacks={lastAttacks} loading={loading} />
          <div className="game__boards">
            <Board
              board={game.playerBoard}
              label="Your board"
              lastShot={lastAttacks?.opponent ?? null}
            />
            <Board
              board={game.opponentBoard}
              label="Enemy waters"
              interactive
              disabled={game.status !== 'IN_PROGRESS' || game.turn !== 'PLAYER' || loading}
              lastShot={lastAttacks?.player ?? null}
              onFire={fire}
            />
          </div>
          <FleetStatus board={game.playerBoard} />
        </div>
      )}
    </main>
  )
}

export default App