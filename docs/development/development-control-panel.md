# Development Control Panel

## Scope

This document describes the slim frontend for bot lifecycle control.

## Implemented

Frontend project: `frontend/control-panel`

- React + Vite app
- no authentication
- reads bot list from `GET /api/bots`
- sends lifecycle actions to `POST /api/bots/{botId}/lifecycle`
- profile selector per action (`SMALL`, `MEDIUM`, `LARGE`)
- auto refresh every 5 seconds

## Local Run

Start backend stack first (or at least `bot-api` on port `8081`).

```powershell
Set-Location "C:\Users\nicop\Documents\Programming\discord-micro-services-platform\frontend\control-panel"
npm install
npm run dev
```

Open `http://localhost:5173`.

## Docker Run

Frontend is included in compose as `control-panel`.

```powershell
Set-Location "C:\Users\nicop\Documents\Programming\discord-micro-services-platform\infrastructure\docker"
docker compose up --build
```

Open `http://localhost:3000`.

## API Dependency

The frontend expects `bot-api` to expose:

- `GET /api/bots`
- `POST /api/bots/{botId}/lifecycle`

