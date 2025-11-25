import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../../api'

export default function Home(){
  const [stats, setStats] = useState({ events: 0, attendees: 0, registrations: 0 })
  const [recentEvents, setRecentEvents] = useState([])

  useEffect(() => {
    const loadStats = async () => {
      try {
        const [eventsRes, attendeesRes, registrationsRes] = await Promise.all([
          api.get('/api/events'),
          api.get('/api/attendees'),
          api.get('/api/registrations')
        ])
        setStats({
          events: (eventsRes.data?.items || eventsRes.data || []).length,
          attendees: (attendeesRes.data?.items || attendeesRes.data || []).length,
          registrations: (registrationsRes.data?.items || registrationsRes.data || []).length
        })
        setRecentEvents((eventsRes.data?.items || eventsRes.data || []).slice(0, 3))
      } catch(e) {
        console.error('Error cargando estadísticas', e)
      }
    }
    loadStats()
  }, [])

  return (
    <div className="space-y-6">
      {/* Hero Section */}
      <div className="card bg-gradient-to-r from-neutral-900 to-neutral-700 text-white">
        <div className="flex items-center gap-4">
          <div className="w-20 h-20 rounded-2xl bg-white/10 backdrop-blur flex items-center justify-center text-4xl">
            🎫
          </div>
          <div>
            <h2 className="text-3xl font-bold mb-2">Bienvenid@ a la Plataforma de Eventos</h2>
            <p className="text-white/80">Gestiona eventos, asistentes, inscripciones e invitaciones en un solo lugar</p>
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid md:grid-cols-3 gap-6">
        <Link to="/eventos" className="card hover:shadow-xl transition cursor-pointer">
          <div className="flex items-center justify-between">
            <div>
              <div className="text-sm text-neutral-500 mb-1">Total Eventos</div>
              <div className="text-3xl font-bold">{stats.events}</div>
            </div>
            <div className="w-12 h-12 rounded-xl bg-blue-100 flex items-center justify-center text-2xl">
              📅
            </div>
          </div>
        </Link>

        <Link to="/asistentes" className="card hover:shadow-xl transition cursor-pointer">
          <div className="flex items-center justify-between">
            <div>
              <div className="text-sm text-neutral-500 mb-1">Total Asistentes</div>
              <div className="text-3xl font-bold">{stats.attendees}</div>
            </div>
            <div className="w-12 h-12 rounded-xl bg-green-100 flex items-center justify-center text-2xl">
              👥
            </div>
          </div>
        </Link>

        <Link to="/inscripciones" className="card hover:shadow-xl transition cursor-pointer">
          <div className="flex items-center justify-between">
            <div>
              <div className="text-sm text-neutral-500 mb-1">Inscripciones</div>
              <div className="text-3xl font-bold">{stats.registrations}</div>
            </div>
            <div className="w-12 h-12 rounded-xl bg-purple-100 flex items-center justify-center text-2xl">
              📝
            </div>
          </div>
        </Link>
      </div>

      {/* Quick Actions */}
      <div className="card">
        <h3 className="text-xl font-semibold mb-4">Acciones Rápidas</h3>
        <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-3">
          <Link to="/eventos" className="btn bg-neutral-900 text-white text-center">
            ➕ Nuevo Evento
          </Link>
          <Link to="/asistentes" className="btn bg-neutral-100 text-center">
            👤 Registrar Asistente
          </Link>
          <Link to="/inscripciones" className="btn bg-neutral-100 text-center">
            📋 Nueva Inscripción
          </Link>
          <Link to="/invitaciones" className="btn bg-neutral-100 text-center">
            🎟️ Generar Invitación
          </Link>
        </div>
      </div>

      {/* Recent Events */}
      {recentEvents.length > 0 && (
        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-xl font-semibold">Eventos Recientes</h3>
            <Link to="/eventos" className="text-sm text-neutral-600 hover:text-neutral-900">Ver todos →</Link>
          </div>
          <div className="space-y-3">
            {recentEvents.map((event, idx) => (
              <div key={idx} className="p-3 rounded-xl border hover:bg-neutral-50 transition">
                <div className="font-medium">{event.title}</div>
                <div className="text-sm text-neutral-500">
                  📍 {event.location} • 📅 {event.date}
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Configuration Info */}
      <div className="card bg-neutral-50">
        <h3 className="text-sm font-semibold mb-2">⚙️ Configuración</h3>
        <p className="text-sm text-neutral-600">
          Backend: <code className="bg-white px-2 py-1 rounded">{import.meta.env.VITE_API_URL || 'http://localhost:7070'}</code>
        </p>
        <p className="text-xs text-neutral-500 mt-2">
          Puedes configurar <code>VITE_API_URL</code> en el archivo <code>.env</code> si tu backend corre en otro puerto.
        </p>
      </div>
    </div>
  )
}
