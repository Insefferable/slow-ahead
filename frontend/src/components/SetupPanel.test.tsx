import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, expect, it, vi } from 'vitest'
import { SetupPanel } from './SetupPanel'

describe('SetupPanel', () => {
  it('delegates random fleet placement', async () => {
    const user = userEvent.setup()
    const onRandom = vi.fn()
    render(<SetupPanel onRandom={onRandom} onStart={vi.fn()} loading={false} />)

    await user.click(screen.getByRole('button', { name: /random fleet/i }))

    expect(onRandom).toHaveBeenCalledTimes(1)
  })

  it('keeps start disabled until the fleet is placed', () => {
    render(<SetupPanel onRandom={vi.fn()} onStart={vi.fn()} loading={false} />)
    expect(screen.getByRole('button', { name: /start battle/i })).toBeDisabled()
  })
})