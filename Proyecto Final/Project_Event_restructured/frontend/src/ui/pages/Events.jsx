import React, { useEffect, useState } from 'react'
import { api } from '../../api'

export default function Events(){
  const [list,setList] = useState([])
  const [form,setForm] = useState({ title:'', description:'', location:'', date:'' })
  const [err,setErr] = useState(null)

  const load = async () => {
    try{ const {data} = await api.get('/api/events'); setList(data?.items || data || []) }catch(e){ setErr(e?.response?.data || e.message) }
  }
  useEffect(()=>{ load() }, [])

  const create = async (e) => {
    e.preventDefault()
    try{ await api.post('/api/events', form); setForm({ title:'', description:'', location:'', date:''}); load() }catch(e){ setErr(e?.response?.data || e.message) }
  }

  return (
    <div className="grid md:grid-cols-2 gap-6">
      <div className="card">
        <h2 className="text-xl font-semibold mb-3">Crear Evento</h2>
        <form onSubmit={create} className="grid gap-3">
          <input className="input" placeholder="Título" value={form.title} onChange={e=>setForm({...form, title:e.target.value})} />
          <input className="input" placeholder="Lugar" value={form.location} onChange={e=>setForm({...form, location:e.target.value})} />
          <input className="input" placeholder="Fecha (YYYY-MM-DD)" value={form.date} onChange={e=>setForm({...form, date:e.target.value})} />
          <textarea className="input h-28" placeholder="Descripción" value={form.description} onChange={e=>setForm({...form, description:e.target.value})} />
          <button className="btn bg-neutral-900 text-white">Guardar</button>
        </form>
        {err && <pre className="bg-red-50 text-red-700 p-3 rounded-xl mt-3 text-sm overflow-x-auto">{JSON.stringify(err,null,2)}</pre>}
      </div>
      <div className="card">
        <h2 className="text-xl font-semibold mb-3">Eventos</h2>
        <ul className="space-y-2">
          {list.map((e,idx)=>(
            <li key={idx} className="p-3 rounded-xl border flex items-center justify-between">
              <div>
                <div className="font-medium">{e.title || e.name}</div>
                <div className="text-sm text-neutral-500">{e.location} · {e.date}</div>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  )
}
