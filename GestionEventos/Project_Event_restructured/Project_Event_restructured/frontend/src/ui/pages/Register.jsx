import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { api } from '../../api'

export default function Register(){
  const [name,setName] = useState('')
  const [email,setEmail] = useState('')
  const [password,setPassword] = useState('')
  const [confirmPassword,setConfirmPassword] = useState('')
  const [err,setErr] = useState(null)
  const [loading,setLoading] = useState(false)
  const navigate = useNavigate()

  const submit = async (e) => {
    e.preventDefault()
    setErr(null)

    if(password !== confirmPassword) {
      setErr('Las contraseñas no coinciden')
      return
    }

    if(password.length < 6) {
      setErr('La contraseña debe tener al menos 6 caracteres')
      return
    }

    setLoading(true)
    try{
      const {data} = await api.post('/api/users/register', { name, email, password })
      if(data) {
        navigate('/login')
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
            👤
          </div>
          <h2 className="text-2xl font-bold mb-1">Crear cuenta</h2>
          <p className="text-neutral-500 text-sm">Regístrate para gestionar tus eventos</p>
        </div>

        <form onSubmit={submit} className="grid gap-4">
          <div>
            <label className="block text-sm font-medium mb-1">Nombre completo</label>
            <input 
              className="input w-full" 
              placeholder="Tu nombre" 
              required
              value={name} 
              onChange={e=>setName(e.target.value)} 
            />
          </div>

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
              placeholder="Mínimo 6 caracteres" 
              type="password"
              required
              value={password} 
              onChange={e=>setPassword(e.target.value)} 
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Confirmar contraseña</label>
            <input 
              className="input w-full" 
              placeholder="Repite tu contraseña" 
              type="password"
              required
              value={confirmPassword} 
              onChange={e=>setConfirmPassword(e.target.value)} 
            />
          </div>
          
          <button 
            type="submit"
            disabled={loading}
            className="btn bg-neutral-900 text-white w-full disabled:opacity-50"
          >
            {loading ? 'Registrando...' : 'Crear cuenta'}
          </button>
        </form>

        {err && (
          <div className="bg-red-50 text-red-700 p-3 rounded-xl mt-4 text-sm">
            {err}
          </div>
        )}

        <div className="mt-6 text-center text-sm text-neutral-600">
          ¿Ya tienes cuenta? <Link to="/login" className="text-neutral-900 font-medium hover:underline">Inicia sesión</Link>
        </div>
      </div>
    </div>
  )
}
