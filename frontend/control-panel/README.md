# Control Panel

Slim React control panel without authentication.

## Features

- load current bot list from `bot-api`
- display live status and capabilities
- trigger `START` and `STOP` lifecycle actions
- select resource profile (`SMALL`, `MEDIUM`, `LARGE`) per action

## Local Development

```powershell
Set-Location "C:\Users\nicop\Documents\Programming\discord-micro-services-platform\frontend\control-panel"
npm install
npm run dev
```

Vite runs on `http://localhost:5173` and proxies `/api` to `http://localhost:8081`.

## Build

```powershell
Set-Location "C:\Users\nicop\Documents\Programming\discord-micro-services-platform\frontend\control-panel"
npm run build
```

## Docker

The compose stack exposes the frontend at `http://localhost:3000`.

