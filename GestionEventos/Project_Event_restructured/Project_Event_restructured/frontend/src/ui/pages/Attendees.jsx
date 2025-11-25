import React, { useEffect, useState } from 'react'
import { api } from '../../api'

export default function Attendees(){
  const [list,setList] = useState([])
  const [events,setEvents] = useState([])
  const [form,setForm] = useState({ name:'', email:'', eventId:'', phone:'' })
  const [editId, setEditId] = useState(null)
  const [search, setSearch] = useState('')
  const [err,setErr] = useState(null)
  const [msg,setMsg] = useState(null)

  const load = async () => {
    try{ 
      const {data} = await api.get('/api/attendees')
      setList(data?.items || data || [])
      setErr(null)
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const loadEvents = async () => {
    try{
      const {data} = await api.get('/api/events')
      setEvents(data?.items || data || [])
    }catch(e){ console.error(e) }
  }

  useEffect(()=>{ 
    load()
    loadEvents()
  }, [])

  const create = async (e) => {
    e.preventDefault()
    setErr(null)
    setMsg(null)
    try{ 
      await api.post('/api/attendees', form)
      setForm({ name:'', email:'', eventId:'', phone:''})
      setMsg('Asistente registrado exitosamente')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const update = async (e) => {
    e.preventDefault()
    setErr(null)
    setMsg(null)
    try{
      await api.put(`/api/attendees/${editId}`, form)
      setEditId(null)
      setForm({ name:'', email:'', eventId:'', phone:''})
      setMsg('Asistente actualizado exitosamente')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const deleteAttendee = async (id) => {
    if(!confirm('¿Eliminar este asistente?')) return
    setErr(null)
    setMsg(null)
    try{
      await api.delete(`/api/attendees/${id}`)
      setMsg('Asistente eliminado')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const editAttendee = (attendee) => {
    setEditId(attendee.id || attendee._id)
    setForm({
      name: attendee.name || '',
      email: attendee.email || '',
      eventId: attendee.eventId || '',
      phone: attendee.phone || ''
    })
    setErr(null)
    setMsg(null)
  }

  const cancelEdit = () => {
    setEditId(null)
    setForm({ name:'', email:'', eventId:'', phone:''})
    setErr(null)
    setMsg(null)
  }

  const filtered = list.filter(a => 
    !search || (a.name||'').toLowerCase().includes(search.toLowerCase()) ||
    (a.email||'').toLowerCase().includes(search.toLowerCase())
  )

  const getEventName = (eventId) => {
    const event = events.find(e => (e.id || e._id) === eventId)
    return event ? event.title : eventId
  }

  return (
    <div className="grid lg:grid-cols-2 gap-6">
      <div className="card">
        <h2 className="text-xl font-semibold mb-3">{editId ? 'Editar' : 'Registrar'} Asistente</h2>
        <form onSubmit={editId ? update : create} className="grid gap-3">
          <input className="input" placeholder="Nombre *" required value={form.name} onChange={e=>setForm({...form, name:e.target.value})} />
          <input className="input" placeholder="Email *" required type="email" value={form.email} onChange={e=>setForm({...form, email:e.target.value})} />
          <input className="input" placeholder="Teléfono" value={form.phone} onChange={e=>setForm({...form, phone:e.target.value})} />
          <select className="input" value={form.eventId} onChange={e=>setForm({...form, eventId:e.target.value})}>
            <option value="">Seleccionar Evento (opcional)</option>
            {events.map((ev,i)=>(
              <option key={i} value={ev.id || ev._id}>{ev.title}</option>
            ))}
          </select>
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
          <h2 className="text-xl font-semibold">Asistentes ({filtered.length})</h2>
          <button onClick={load} className="btn bg-neutral-100 text-sm">↻ Recargar</button>
        </div>
        <input 
          className="input mb-3 w-full" 
          placeholder="Buscar por nombre o email..." 
          value={search} 
          onChange={e=>setSearch(e.target.value)} 
        />
        <div className="space-y-2 max-h-[600px] overflow-y-auto">
          {filtered.length === 0 ? (
            <div className="text-neutral-400 text-center py-8">No hay asistentes</div>
          ) : (
            filtered.map((a,idx)=>(
              <div key={idx} className="p-4 rounded-xl border hover:shadow-md transition">
                <div className="flex items-start justify-between mb-2">
                  <div className="flex-1">
                    <div className="font-semibold">{a.name}</div>
                    <div className="text-sm text-neutral-600">📧 {a.email}</div>
                    {a.phone && <div className="text-sm text-neutral-600">📱 {a.phone}</div>}
                    {a.eventId && (
                      <div className="text-xs text-neutral-500 mt-1">Evento: {getEventName(a.eventId)}</div>
                    )}
                  </div>
                </div>
                <div className="flex gap-2 mt-3">
                  <button onClick={()=>editAttendee(a)} className="btn bg-blue-100 text-blue-700 text-sm">Editar</button>
                  <button onClick={()=>deleteAttendee(a.id || a._id)} className="btn bg-red-100 text-red-700 text-sm">Eliminar</button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  )
}
