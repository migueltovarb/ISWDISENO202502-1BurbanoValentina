import React from 'react'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import Shell from './Shell.jsx'
import Home from './pages/Home.jsx'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import Events from './pages/Events.jsx'
import Attendees from './pages/Attendees.jsx'
import Invitations from './pages/Invitations.jsx'
import Registrations from './pages/Registrations.jsx'

const router = createBrowserRouter([
  { path: '/', element: <Shell />, children: [
    { index: true, element: <Home /> },
    { path: 'login', element: <Login /> },
    { path: 'registro', element: <Register /> },
    { path: 'eventos', element: <Events /> },
    { path: 'asistentes', element: <Attendees /> },
    { path: 'invitaciones', element: <Invitations /> },
    { path: 'inscripciones', element: <Registrations /> },
  ]}
])

export default function AppRouter(){ return <RouterProvider router={router} /> }
