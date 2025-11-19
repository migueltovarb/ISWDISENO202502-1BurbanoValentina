import React, { useState } from 'react'
import { api, setAuthToken } from '../../api'

export default function Login(){
  const [email,setEmail] = useState('')
  const [password,setPassword] = useState('')
  const [out,setOut] = useState(null)
  const [err,setErr] = useState(null)

  const submit = async (e) => {
    e.preventDefault(); setErr(null); setOut(null)
    try{
      const {data} = await api.post('/api/users/login', { email, password })
      setOut(data)
      if (data?.token) setAuthToken(data.token)
    }catch(e){ setErr(e?.response?.data || e.message) }
  }

  return (
    <div className="card max-w-lg">
      <h2 className="text-xl font-semibold mb-3">Iniciar sesión</h2>
      <form onSubmit={submit} className="grid gap-3">
        <input className="input" placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} />
        <input className="input" placeholder="Contraseña" type="password" value={password} onChange={e=>setPassword(e.target.value)} />
        <button className="btn bg-neutral-900 text-white">Entrar</button>
      </form>
      <pre className="bg-neutral-50 p-3 rounded-xl mt-3 text-sm overflow-x-auto">{out? JSON.stringify(out,null,2) : '—'}</pre>
      {err && <pre className="bg-red-50 text-red-700 p-3 rounded-xl mt-2 text-sm overflow-x-auto">{JSON.stringify(err,null,2)}</pre>}
    </div>
  )
}
