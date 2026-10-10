## How to play

On a 10×10 board you command a five-ship fleet:
Carrier (5), Battleship (4), Cruiser (3), Submarine (3), Destroyer (2).

1. Place your ships — or let the app randomize them.
2. Take turns firing at enemy waters; the AI replies after every shot.
3. Sink the entire enemy fleet before yours is destroyed.

## Tech stack

| Component   | Stack                                                       |
| ----------- | ----------------------------------------------------------- |
| backend     | Java 8, Spring Boot 2.7.18, Maven wrapper, Firebase Admin 9 |
| persistence | Cloud Firestore (in-memory store by default for local dev)  |
| frontend    | React 19, Vite 8, TypeScript, Vitest                        |

## Prerequisites

- JDK 8 (e.g. Azul Zulu 8)
- Node.js 20+
- On PowerShell with a Restricted execution policy use `npm.cmd`, not `npm`.

## Quickstart

Backend — REST API on http://localhost:8080:

```powershell
cd backend
./mvnw spring-boot:run
Frontend — dev server on http://localhost:5173, proxies /api to :8080:
cd frontend
npm.cmd install
npm.cmd run dev
Run both, then open http://localhost:5173.
Firebase persistence (optional)
By default games are stored in memory. To use Firestore, set the variables from
backend/.env.example:
FIREBASE_ENABLED=true
FIREBASE_PROJECT_ID=your-project-id
GOOGLE_APPLICATION_CREDENTIALS=C:/path/to/service-account.json
Or run against the local emulator with FIRESTORE_EMULATOR_HOST=localhost:8080.
API
Method	Path	Request	Response
POST	/api/games	{ "randomFleet": true } or { "ships": [...] }	201 GameState
GET	/api/games/{id}	–	200 GameState
POST	/api/games/{id}/moves	{ "row": 0, "col": 0 }	200 MoveResult
DELETE	/api/games/{id}	–	204
Errors: 400 invalid placement/attack, 404 unknown game, 409 game over.
POST /api/games
{ "randomFleet": true }

201 Created
{
  "gameId": "…",
  "status": "IN_PROGRESS",
  "turn": "PLAYER",
  "playerBoard":  { "size": 10, "cells": [["EMPTY", …]], "ships": […] },
  "opponentBoard": { "size": 10, "cells": [["EMPTY", …]], "ships": [] }
}
Move results return playerAttack and opponentAttack (the AI reply) plus the
updated state; the opponent board never reveals unhit ships. Ships are placed
with { "type", "row", "col", "orientation": "HORIZONTAL"|"VERTICAL" }.
Testing
cd backend;  ./mvnw -B clean verify
cd frontend; npm.cmd run lint; npm.cmd run test; npm.cmd run build
CI runs these on every push/PR. Work flows through the dev branch via
short-lived feature/* branches and squash PRs with Conventional Commit messages.