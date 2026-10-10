const GRID_SIZE = 10

function App() {
  const cells = Array.from({ length: GRID_SIZE * GRID_SIZE }, (_, i) => i)

  return (
    <main className="app">
      <h1>Battleship</h1>
      <section className="board" role="grid" aria-label="board placeholder">
        {cells.map((i) => (
          <div key={i} className="cell" />
        ))}
      </section>
    </main>
  )
}

export default App