import React, { useState, useEffect } from 'react'
import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom'
import { setAuthToken } from '../api'

const NavLink = ({to, children}) => {
  const loc = useLocation()
  const active = loc.pathname === to
  return <Link to={to} className={"px-3 py-2 rounded-xl transition " + (active? "bg-neutral-900 text-white":"hover:bg-neutral-100")}>{children}</Link>
}

export default function Shell(){
  const [user, setUser] = useState(null)
  const [showMobileMenu, setShowMobileMenu] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    // Verificar si hay usuario guardado en localStorage
    const savedUser = localStorage.getItem('user')
    const savedToken = localStorage.getItem('token')
    if(savedUser && savedToken) {
      setUser(JSON.parse(savedUser))
      setAuthToken(savedToken)
    }
  }, [])

  const handleLogout = () => {
    localStorage.removeItem('user')
    localStorage.removeItem('token')
    setAuthToken(null)
    setUser(null)
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-neutral-50 to-neutral-100">
      <header className="bg-white shadow-sm border-b border-neutral-200 sticky top-0 z-50">
        <div className="container">
          <div className="py-4 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-neutral-900 text-white flex items-center justify-center font-bold">
                🎫
              </div>
              <h1 className="text-2xl font-bold tracking-tight">Plataforma de Eventos</h1>
            </div>

            {/* Desktop Navigation */}
            <nav className="hidden md:flex gap-2 items-center">
              <NavLink to="/">Inicio</NavLink>
              <NavLink to="/eventos">Eventos</NavLink>
              <NavLink to="/asistentes">Asistentes</NavLink>
              <NavLink to="/inscripciones">Inscripciones</NavLink>
              <NavLink to="/invitaciones">Invitaciones</NavLink>
              {user ? (
                <div className="flex items-center gap-2 ml-2 pl-2 border-l">
                  <span className="text-sm text-neutral-600">👤 {user.name || user.email}</span>
                  <button onClick={handleLogout} className="btn bg-red-100 text-red-700 text-sm">Salir</button>
                </div>
              ) : (
                <>
                  <NavLink to="/login">Login</NavLink>
                  <NavLink to="/registro">Registro</NavLink>
                </>
              )}
            </nav>

            {/* Mobile Menu Button */}
            <button 
              onClick={() => setShowMobileMenu(!showMobileMenu)}
              className="md:hidden btn bg-neutral-100"
            >
              ☰
            </button>
          </div>

          {/* Mobile Menu */}
          {showMobileMenu && (
            <nav className="md:hidden pb-4 flex flex-col gap-2">
              <NavLink to="/">Inicio</NavLink>
              <NavLink to="/eventos">Eventos</NavLink>
              <NavLink to="/asistentes">Asistentes</NavLink>
              <NavLink to="/inscripciones">Inscripciones</NavLink>
              <NavLink to="/invitaciones">Invitaciones</NavLink>
              {user ? (
                <>
                  <div className="text-sm text-neutral-600 px-3 py-2">👤 {user.name || user.email}</div>
                  <button onClick={handleLogout} className="btn bg-red-100 text-red-700 text-left">Salir</button>
                </>
              ) : (
                <>
                  <NavLink to="/login">Login</NavLink>
                  <NavLink to="/registro">Registro</NavLink>
                </>
              )}
            </nav>
          )}
        </div>
      </header>

      <main className="container py-6">
        <Outlet />
      </main>

      <footer className="container py-6 text-center text-sm text-neutral-500 border-t border-neutral-200 mt-12">
        <p>Plataforma de Gestión de Eventos © 2025</p>
      </footer>
    </div>
  )
}
