import React, { useEffect, useState } from 'react'
import { api } from '../../api'

export default function Registrations(){
  const [list,setList] = useState([])
  const [form,setForm] = useState({ attendeeId:'', eventId:'' })
  const [err,setErr] = useState(null)

  const load = async () => {
    try{ const {data} = await api.get('/api/registrations'); setList(data?.items || data || []) }catch(e){ setErr(e?.response?.data || e.message) }
  }
  useEffect(()=>{ load() }, [])

  const create = async (e) => {
    e.preventDefault()
    try{ await api.post('/api/registrations', form); setForm({ attendeeId:'', eventId:''}); load() }catch(e){ setErr(e?.response?.data || e.message) }
  }

  return (
    <div className="grid md:grid-cols-2 gap-6">
      <div className="card">
        <h2 className="text-xl font-semibold mb-3">Nueva Inscripción</h2>
        <form onSubmit={create} className="grid gap-3">
          <input className="input" placeholder="ID Asistente" value={form.attendeeId} onChange={e=>setForm({...form, attendeeId:e.target.value})} />
          <input className="input" placeholder="ID Evento" value={form.eventId} onChange={e=>setForm({...form, eventId:e.target.value})} />
          <button className="btn bg-neutral-900 text-white">Guardar</button>
        </form>
        {err && <pre className="bg-red-50 text-red-700 p-3 rounded-xl mt-3 text-sm overflow-x-auto">{JSON.stringify(err,null,2)}</pre>}
      </div>
      <div className="card">
        <h2 className="text-xl font-semibold mb-3">Inscripciones</h2>
        <ul className="space-y-2">
          {list.map((r,idx)=>(
            <li key={idx} className="p-3 rounded-xl border">
              <div>Asistente: {r.attendeeId} · Evento: {r.eventId}</div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  )
}
