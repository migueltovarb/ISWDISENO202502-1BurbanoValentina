# 🎫 Event Platform Frontend

Interfaz de usuario moderna y responsiva para la plataforma de gestión de eventos, construida con React, Vite y TailwindCSS.

## 🚀 Características

- ✅ **Gestión completa de eventos**: Crear, editar, eliminar y buscar eventos
- 👥 **Administración de asistentes**: Registro y gestión de participantes
- 📋 **Control de inscripciones**: Sistema de inscripciones con estados (Pendiente, Confirmada, Cancelada)
- 🎟️ **Códigos de invitación**: Generación y gestión de códigos únicos
- 🔐 **Autenticación**: Sistema de login y registro de usuarios
- 📱 **Diseño responsivo**: Funciona perfectamente en desktop, tablet y móvil
- 🎨 **UI moderna**: Interfaz limpia y profesional con TailwindCSS
- ⚡ **Rendimiento optimizado**: Construcción rápida con Vite

## 📋 Prerequisitos

- Node.js (v16 o superior)
- npm o yarn
- Backend de la aplicación ejecutándose (por defecto en `http://localhost:7070`)

## 🛠️ Instalación

1. Instala las dependencias:
```bash
npm install
# o
yarn install
# o
pnpm install
```

2. Configura las variables de entorno:
```bash
cp .env.example .env
```

Edita `.env` si tu backend corre en un puerto diferente:
```env
VITE_API_URL=http://localhost:7070
```

## 🎯 Uso

### Modo desarrollo
```bash
npm run dev
```
La aplicación estará disponible en `http://localhost:5173`

### Construcción para producción
```bash
npm run build
```

### Vista previa de la construcción
```bash
npm run preview
```

## 📁 Estructura del Proyecto

```
frontend/
├── src/
│   ├── api.js              # Configuración de Axios
│   ├── main.jsx            # Punto de entrada
│   └── ui/
│       ├── App.jsx         # Componente principal
│       ├── Shell.jsx       # Layout y navegación
│       ├── router.jsx      # Configuración de rutas
│       ├── index.css       # Estilos globales
│       └── pages/
│           ├── Home.jsx            # Dashboard principal
│           ├── Events.jsx          # Gestión de eventos
│           ├── Attendees.jsx       # Gestión de asistentes
│           ├── Registrations.jsx   # Gestión de inscripciones
│           ├── Invitations.jsx     # Códigos de invitación
│           ├── Login.jsx           # Inicio de sesión
│           └── Register.jsx        # Registro de usuarios
├── index.html
├── package.json
├── vite.config.js
└── tailwind.config.js
```

## 🎨 Tecnologías

- **React 18** - Biblioteca de UI
- **React Router DOM 6** - Enrutamiento
- **Axios** - Cliente HTTP
- **Vite** - Build tool y dev server
- **TailwindCSS** - Framework de estilos

## 📝 Funcionalidades por Página

### 🏠 Home
- Dashboard con estadísticas
- Accesos rápidos a funciones principales
- Visualización de eventos recientes

### 📅 Eventos
- Crear nuevos eventos con título, lugar, fecha, capacidad y descripción
- Editar eventos existentes
- Eliminar eventos
- Búsqueda en tiempo real

### 👥 Asistentes
- Registrar nuevos asistentes (nombre, email, teléfono)
- Asignar asistentes a eventos
- Editar y eliminar asistentes
- Búsqueda por nombre o email

### 📋 Inscripciones
- Crear inscripciones relacionando asistentes con eventos
- Gestión de estados (Pendiente, Confirmada, Cancelada)
- Filtrado por estado

### 🎟️ Invitaciones
- Generar códigos de invitación únicos
- Asignar códigos a eventos específicos
- Control de usos máximos

## 🔧 Integración con el Backend

El frontend espera que el backend esté disponible en las siguientes rutas:

- `POST /api/users/register` - Registro de usuarios
- `POST /api/users/login` - Inicio de sesión
- `GET/POST/PUT/DELETE /api/events` - CRUD de eventos
- `GET/POST/PUT/DELETE /api/attendees` - CRUD de asistentes
- `GET/POST/PUT/DELETE /api/registrations` - CRUD de inscripciones
- `GET/POST/PUT/DELETE /api/invitation-codes` - CRUD de códigos

## 📄 Licencia

Este proyecto es parte de la Plataforma de Gestión de Eventos.
