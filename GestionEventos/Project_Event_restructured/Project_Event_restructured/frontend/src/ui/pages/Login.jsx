import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { api, setAuthToken } from '../../api'

export default function Login(){
  const [email,setEmail] = useState('')
  const [password,setPassword] = useState('')
  const [err,setErr] = useState(null)
  const [loading,setLoading] = useState(false)
  const navigate = useNavigate()

  const submit = async (e) => {
    e.preventDefault()
    setErr(null)
    setLoading(true)
    try{
      const {data} = await api.post('/api/users/login', { email, password })
      if (data?.token) {
        setAuthToken(data.token)
        localStorage.setItem('token', data.token)
        localStorage.setItem('user', JSON.stringify(data.user || {email}))
        navigate('/eventos')
        window.location.reload() // Recargar para actualizar el Shell
      } else {
        setErr('No se recibió token de autenticación')
      }
    }catch(e){ 
      setErr(e?.response?.data?.message || e.message) 
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex items-center justify-center min-h-[70vh]">
      <div className="card max-w-md w-full">
        <div className="text-center mb-6">
          <div className="w-16 h-16 rounded-2xl bg-neutral-900 text-white flex items-center justify-center text-3xl mx-auto mb-3">
            🔐
          </div>
          <h2 className="text-2xl font-bold mb-1">Iniciar sesión</h2>
          <p className="text-neutral-500 text-sm">Accede a tu cuenta para gestionar eventos</p>
        </div>

        <form onSubmit={submit} className="grid gap-4">
          <div>
            <label className="block text-sm font-medium mb-1">Email</label>
            <input 
              className="input w-full" 
              placeholder="tu@email.com" 
              type="email"
              required
              value={email} 
              onChange={e=>setEmail(e.target.value)} 
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Contraseña</label>
            <input 
              className="input w-full" 
              placeholder="••••••••" 
              type="password"
              required
              value={password} 
              onChange={e=>setPassword(e.target.value)} 
            />
          </div>
          
          <button 
            type="submit" 
            disabled={loading}
            className="btn bg-neutral-900 text-white w-full disabled:opacity-50"
          >
            {loading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        {err && (
          <div className="bg-red-50 text-red-700 p-3 rounded-xl mt-4 text-sm">
            {err}
          </div>
        )}

        <div className="mt-6 text-center text-sm text-neutral-600">
          ¿No tienes cuenta? <Link to="/registro" className="text-neutral-900 font-medium hover:underline">Regístrate aquí</Link>
        </div>
      </div>
    </div>
  )
}
