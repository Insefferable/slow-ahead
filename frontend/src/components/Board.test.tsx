import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, expect, it, vi } from 'vitest'
import { Board } from './Board'
import type { BoardView } from '../api/types'

const board: BoardView = {
  size: 2,
  cells: [
    ['EMPTY', 'SHIP'],
    ['HIT', 'MISS'],
  ],
  ships: [],
}

describe('Board', () => {
  it('renders one grid cell per coordinate', () => {
    render(<Board board={board} label="Your board" />)
    expect(screen.getAllByRole('gridcell')).toHaveLength(4)
  })

  it('fires only from unattacked target cells', async () => {
    const user = userEvent.setup()
    const onFire = vi.fn()
    render(<Board board={board} label="Enemy waters" interactive onFire={onFire} />)

    const cells = screen.getAllByRole('gridcell')
    await user.click(cells[0])
    await user.click(cells[2])

    expect(onFire).toHaveBeenCalledTimes(1)
    expect(onFire).toHaveBeenCalledWith(0, 0)
  })
})