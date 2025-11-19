# Project Event (Monorepo: backend + frontend)

Este paquete reestructura tu proyecto original dentro de `backend/` y agrega un **frontend moderno** (Vite + React + Tailwind) en `frontend/`.

- **Frontend UI:** Español (como pediste). 
- **Backend:** no se altera la lógica; sólo se ajusta CORS y soporte `.env` si aplica.
- **DB:** usa la configuración ya presente en tu backend. Asegúrate de definir variables en `.env` si tu backend las requiere.

## Cómo ejecutar

1) Backend
   - Entra a `backend/` y sigue las instrucciones de ese proyecto.
   - Si es Python/Flask/FastAPI:
     ```bash
     python -m venv .venv
     source .venv/bin/activate  # Windows: .venv\Scripts\activate
     pip install -r requirements.txt
     # Variables de entorno en .env si corresponde
     python app.py # o uvicorn main:app --reload
     ```

2) Frontend
   ```bash
   cd frontend
   pnpm i  # o npm i / yarn
   pnpm dev
   ```

3) Conexión
   - El frontend usa `VITE_API_URL` (por defecto `http://localhost:8000`).
   - Asegúrate de que el backend responda a `/health` (opcional) o a tus rutas reales (`/api`, `/api/v1`, etc.).

> Si necesitas pantallas específicas para tus endpoints, dímelos y te maqueto vistas hermosas y conectadas 1:1.
