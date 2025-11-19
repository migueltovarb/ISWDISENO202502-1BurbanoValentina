import React from 'react'
import { Link, Outlet, useLocation } from 'react-router-dom'

const NavLink = ({to, children}) => {
  const loc = useLocation()
  const active = loc.pathname === to
  return <Link to={to} className={"px-3 py-2 rounded-xl " + (active? "bg-neutral-900 text-white":"hover:bg-neutral-100")}>{children}</Link>
}

export default function Shell(){
  return (
    <div className="container">
      <header className="mb-6 flex items-center justify-between">
        <h1 className="title">Plataforma de Eventos</h1>
        <nav className="flex gap-2">
          <NavLink to="/">Inicio</NavLink>
          <NavLink to="/eventos">Eventos</NavLink>
          <NavLink to="/asistentes">Asistentes</NavLink>
          <NavLink to="/inscripciones">Inscripciones</NavLink>
          <NavLink to="/invitaciones">Invitaciones</NavLink>
          <NavLink to="/login">Login</NavLink>
        </nav>
      </header>
      <Outlet />
    </div>
  )
}
