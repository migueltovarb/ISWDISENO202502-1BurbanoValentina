import React, { useEffect, useState } from 'react'
import { api } from '../../api'

export default function Events(){
  const [list,setList] = useState([])
  const [form,setForm] = useState({ title:'', description:'', location:'', date:'', maxAttendees:'' })
  const [editId, setEditId] = useState(null)
  const [search, setSearch] = useState('')
  const [err,setErr] = useState(null)
  const [msg,setMsg] = useState(null)

  const load = async () => {
    try{ 
      const {data} = await api.get('/api/events')
      setList(data?.items || data || [])
      setErr(null)
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }
  
  useEffect(()=>{ load() }, [])

  const create = async (e) => {
    e.preventDefault()
    setErr(null)
    setMsg(null)
    try{ 
      await api.post('/api/events', form)
      setForm({ title:'', description:'', location:'', date:'', maxAttendees:''})
      setMsg('Evento creado exitosamente')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const update = async (e) => {
    e.preventDefault()
    setErr(null)
    setMsg(null)
    try{
      await api.put(`/api/events/${editId}`, form)
      setEditId(null)
      setForm({ title:'', description:'', location:'', date:'', maxAttendees:''})
      setMsg('Evento actualizado exitosamente')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const deleteEvent = async (id) => {
    if(!confirm('¿Eliminar este evento?')) return
    setErr(null)
    setMsg(null)
    try{
      await api.delete(`/api/events/${id}`)
      setMsg('Evento eliminado')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const editEvent = (event) => {
    setEditId(event.id || event._id)
    setForm({
      title: event.title || '',
      description: event.description || '',
      location: event.location || '',
      date: event.date || '',
      maxAttendees: event.maxAttendees || ''
    })
    setErr(null)
    setMsg(null)
  }

  const cancelEdit = () => {
    setEditId(null)
    setForm({ title:'', description:'', location:'', date:'', maxAttendees:''})
    setErr(null)
    setMsg(null)
  }

  const filtered = list.filter(e => 
    !search || (e.title||'').toLowerCase().includes(search.toLowerCase()) ||
    (e.location||'').toLowerCase().includes(search.toLowerCase())
  )

  return (
    <div className="grid lg:grid-cols-2 gap-6">
      <div className="card">
        <h2 className="text-xl font-semibold mb-3">{editId ? 'Editar' : 'Crear'} Evento</h2>
        <form onSubmit={editId ? update : create} className="grid gap-3">
          <input className="input" placeholder="Título *" required value={form.title} onChange={e=>setForm({...form, title:e.target.value})} />
          <input className="input" placeholder="Lugar *" required value={form.location} onChange={e=>setForm({...form, location:e.target.value})} />
          <input className="input" placeholder="Fecha (YYYY-MM-DD) *" required type="date" value={form.date} onChange={e=>setForm({...form, date:e.target.value})} />
          <input className="input" placeholder="Capacidad máxima" type="number" value={form.maxAttendees} onChange={e=>setForm({...form, maxAttendees:e.target.value})} />
          <textarea className="input h-28" placeholder="Descripción" value={form.description} onChange={e=>setForm({...form, description:e.target.value})} />
          <div className="flex gap-2">
            <button type="submit" className="btn bg-neutral-900 text-white flex-1">{editId ? 'Actualizar' : 'Guardar'}</button>
            {editId && <button type="button" onClick={cancelEdit} className="btn bg-neutral-200">Cancelar</button>}
          </div>
        </form>
        {msg && <div className="bg-green-50 text-green-700 p-3 rounded-xl mt-3 text-sm">{msg}</div>}
        {err && <div className="bg-red-50 text-red-700 p-3 rounded-xl mt-3 text-sm">{err}</div>}
      </div>

      <div className="card">
        <div className="flex items-center justify-between mb-3">
          <h2 className="text-xl font-semibold">Eventos ({filtered.length})</h2>
          <button onClick={load} className="btn bg-neutral-100 text-sm">↻ Recargar</button>
        </div>
        <input 
          className="input mb-3 w-full" 
          placeholder="Buscar por título o lugar..." 
          value={search} 
          onChange={e=>setSearch(e.target.value)} 
        />
        <div className="space-y-2 max-h-[600px] overflow-y-auto">
          {filtered.length === 0 ? (
            <div className="text-neutral-400 text-center py-8">No hay eventos</div>
          ) : (
            filtered.map((e,idx)=>(
              <div key={idx} className="p-4 rounded-xl border hover:shadow-md transition">
                <div className="flex items-start justify-between mb-2">
                  <div className="flex-1">
                    <div className="font-semibold text-lg">{e.title || e.name}</div>
                    <div className="text-sm text-neutral-600 mt-1">
                      📍 {e.location} • 📅 {e.date}
                    </div>
                    {e.maxAttendees && (
                      <div className="text-xs text-neutral-500 mt-1">Capacidad: {e.maxAttendees} personas</div>
                    )}
                  </div>
                </div>
                {e.description && (
                  <p className="text-sm text-neutral-600 mb-3 line-clamp-2">{e.description}</p>
                )}
                <div className="flex gap-2">
                  <button onClick={()=>editEvent(e)} className="btn bg-blue-100 text-blue-700 text-sm">Editar</button>
                  <button onClick={()=>deleteEvent(e.id || e._id)} className="btn bg-red-100 text-red-700 text-sm">Eliminar</button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  )
}
