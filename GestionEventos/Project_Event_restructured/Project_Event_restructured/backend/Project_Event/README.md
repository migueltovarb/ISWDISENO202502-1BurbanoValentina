# Backend — Event Platform (Javalin + SQLite)

**Stack:** Java 17, Javalin 5, SQLite (HikariCP), Jackson, jBCrypt.  
**Puerto por defecto:** `7070` (configurable).  
**DB:** archivo SQLite en `data/events.db` (configurable).

## Ejecutar (rápido, usando JAR existente)
```bash
cd backend/Project_Event
java -jar target/event-platform-1.0.0-shaded.jar
# o cambiar el puerto
java -Dapp.port=7070 -jar target/event-platform-1.0.0-shaded.jar
# o usar variable de entorno PORT=7070
```

## Configuración
Variables/propiedades soportadas (archivo `src/main/resources/application.properties` o `-Dprop` o variables env):

- `app.port` / `PORT`: puerto HTTP (default `7070`).
- `db.path`: ruta al archivo SQLite (default `data/events.db`).
- `CORS_ORIGIN`: origen permitido para CORS (default `*`).

> En desarrollo, el frontend usa **proxy** de Vite y no requiere CORS en el backend.

## Endpoints principales (resumen)
- `GET /health` — estado del servicio.
- `POST /api/users/register` — registro.
- `POST /api/users/login` — login.
- `GET/POST /api/events` — listar/crear eventos.
- `PUT /api/events/{id}` — actualizar evento.
- `GET/POST /api/attendees` — listar/crear asistentes.
- `PUT /api/attendees/{id}` — actualizar asistente.
- `GET/POST /api/registrations` — listar/crear inscripciones.
- `POST /api/registrations/{id}/cancel` — cancelar inscripción.
- `GET/POST /api/invitation-codes` — listar/crear códigos.

> La API puede incluir más rutas; consulta `src/main/java/com/example/demo/web/Api.java` para el detalle.

## Construir desde código
Si deseas recompilar:

```bash
./mvnw -q -DskipTests package
java -jar target/event-platform-1.0.0-shaded.jar
```
