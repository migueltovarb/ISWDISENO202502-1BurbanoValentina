import React, { useEffect, useState } from 'react'
import { api } from '../../api'

export default function Attendees(){
  const [list,setList] = useState([])
  const [form,setForm] = useState({ name:'', email:'', eventId:'' })
  const [err,setErr] = useState(null)

  const load = async () => {
    try{ const {data} = await api.get('/api/attendees'); setList(data?.items || data || []) }catch(e){ setErr(e?.response?.data || e.message) }
  }
  useEffect(()=>{ load() }, [])

  const create = async (e) => {
    e.preventDefault()
    try{ await api.post('/api/attendees', form); setForm({ name:'', email:'', eventId:''}); load() }catch(e){ setErr(e?.response?.data || e.message) }
  }

  return (
    <div className="grid md:grid-cols-2 gap-6">
      <div className="card">
        <h2 className="text-xl font-semibold mb-3">Registrar Asistente</h2>
        <form onSubmit={create} className="grid gap-3">
          <input className="input" placeholder="Nombre" value={form.name} onChange={e=>setForm({...form, name:e.target.value})} />
          <input className="input" placeholder="Email" value={form.email} onChange={e=>setForm({...form, email:e.target.value})} />
          <input className="input" placeholder="ID Evento" value={form.eventId} onChange={e=>setForm({...form, eventId:e.target.value})} />
          <button className="btn bg-neutral-900 text-white">Guardar</button>
        </form>
        {err && <pre className="bg-red-50 text-red-700 p-3 rounded-xl mt-3 text-sm overflow-x-auto">{JSON.stringify(err,null,2)}</pre>}
      </div>
      <div className="card">
        <h2 className="text-xl font-semibold mb-3">Asistentes</h2>
        <ul className="space-y-2">
          {list.map((a,idx)=>(
            <li key={idx} className="p-3 rounded-xl border">
              <div className="font-medium">{a.name}</div>
              <div className="text-sm text-neutral-500">{a.email}</div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  )
}
