# Vehículo CRUD con Spring Boot y MongoDB

Este proyecto existe para que puedas levantar, en minutos, un servicio REST que gestiona vehículos sobre MongoDB usando Spring Boot 3.5. Está pensado como una base amigable: sin configuraciones misteriosas y con los archivos mínimos para que puedas extenderlo a tu gusto.

## ¿Qué necesitas?
- **Java 17** (el mismo que exige Spring Boot 3.5).
- **Maven 3.9+**; si no lo tienes instalado puedes usar el wrapper `mvnw.cmd` que ya viene incluido.
- **MongoDB** funcionando en local o en cualquier servicio gestionado.

## Conecta tu base de datos
Abre `src/main/resources/application.properties` y ajusta la URI según tu entorno. Si tienes usuario y contraseña, colócalos en la cadena; si estás en local con acceso libre, basta con dejarla así:

```
spring.data.mongodb.uri=mongodb://usuario:password@localhost:27017/vehiculosdb
```

## Ponlo en marcha
```
mvnw.cmd spring-boot:run
```
En cuanto veas el banner de Spring, puedes entrar a `http://localhost:8080` y empezar a probar el API.

## Endpoints disponibles
| Método | Ruta | ¿Qué hace? |
| --- | --- | --- |
| `GET` | `/api/vehiculos` | Devuelve todos los vehículos registrados |
| `GET` | `/api/vehiculos/{id}` | Busca un vehículo específico |
| `POST` | `/api/vehiculos` | Crea un nuevo vehículo |
| `PUT` | `/api/vehiculos/{id}` | Actualiza los datos de un vehículo |
| `DELETE` | `/api/vehiculos/{id}` | Elimina un vehículo |

Para `POST` y `PUT` envía un JSON como este:

```
{
	"marca": "Toyota",
	"modelo": "Corolla",
	"anio": 2020,
	"precio": 15000
}
```

## Prueba express con curl
```
curl -X POST http://localhost:8080/api/vehiculos \
  -H "Content-Type: application/json" \
  -d '{"marca":"Toyota","modelo":"Corolla","anio":2020,"precio":15000}'

curl http://localhost:8080/api/vehiculos
```

