# Web Demo (React + Vite + TypeScript)

This is a real web demo client for the **Spring Boot REST API** in `../api-spring`.

## Features
- Pages: **Login**, **Products**, **Product Details**, **Cart**
- Advanced product search with filters in the **URL** (`q/type/minPrice/maxPrice/minRating/fuzzy/sort/page/size`)
- `axios` interceptor automatically attaches `Authorization: Bearer <JWT>`

## Quick start
1) Start the API:

```bash
cd ..
./gradlew :api-spring:bootRun
```

2) Start the web demo:

```bash
cd web-react
npm install
npm run dev
```

Open `http://localhost:5173`.

## Configure API base URL
By default the client uses `http://localhost:8080`.

You can override via an env var:

```bash
VITE_API_BASE_URL="http://localhost:8080" npm run dev
```

## Notes
- The demo API currently returns compact product details; reviews are not wired in yet.
- Cart and checkout require login.

## Theme modes
Use the navbar theme selector to switch between:
- Dark
- Light
- Scheduled (light from 07:00 to 18:59, dark otherwise)

Theme choice is persisted in `localStorage`.

## UI/UX polish added
- Responsive premium layout with hero sections, cards, badges, empty states and skeleton loading.
- Improved product list hierarchy: icon, metadata pills, clearer price block, hover states.
- More readable auth flows with separate Sign in / Sign up pages.
- Theme switcher still supports Dark / Light / Scheduled.
