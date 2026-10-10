export type CellState = 'EMPTY' | 'SHIP' | 'HIT' | 'MISS'
export type ShipType = 'CARRIER' | 'BATTLESHIP' | 'CRUISER' | 'SUBMARINE' | 'DESTROYER'
export type Orientation = 'HORIZONTAL' | 'VERTICAL'
export type AttackOutcome = 'MISS' | 'HIT' | 'SUNK'
export type GameStatus = 'IN_PROGRESS' | 'PLAYER_WON' | 'OPPONENT_WON'
export type Turn = 'PLAYER' | 'OPPONENT'

export interface ShipView {
  type: ShipType
  row: number
  col: number
  orientation: Orientation
  sunk: boolean
}

export interface BoardView {
  size: number
  cells: CellState[][]
  ships: ShipView[]
}

export interface GameState {
  gameId: string
  status: GameStatus
  turn: Turn
  playerBoard: BoardView
  opponentBoard: BoardView
}

export interface AttackView {
  row: number
  col: number
  outcome: AttackOutcome
  sunkShipType: ShipType | null
}

export interface MoveResult {
  playerAttack: AttackView
  opponentAttack: AttackView | null
  state: GameState
}

export interface ShipPlacement {
  type: ShipType
  row: number
  col: number
  orientation: Orientation
}