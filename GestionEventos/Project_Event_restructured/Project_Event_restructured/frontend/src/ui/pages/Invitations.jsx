import React, { useEffect, useState } from 'react'
import { api } from '../../api'

export default function Invitations(){
  const [list,setList] = useState([])
  const [events,setEvents] = useState([])
  const [form,setForm] = useState({ code:'', eventId:'', maxUses:'' })
  const [editId, setEditId] = useState(null)
  const [search, setSearch] = useState('')
  const [err,setErr] = useState(null)
  const [msg,setMsg] = useState(null)

  const load = async () => {
    try{ 
      const {data} = await api.get('/api/invitation-codes')
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

  const generateCode = () => {
    const code = 'INV-' + Math.random().toString(36).substring(2, 10).toUpperCase()
    setForm({...form, code})
  }

  const create = async (e) => {
    e.preventDefault()
    setErr(null)
    setMsg(null)
    try{ 
      await api.post('/api/invitation-codes', form)
      setForm({ code:'', eventId:'', maxUses:''})
      setMsg('Código de invitación creado')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const update = async (e) => {
    e.preventDefault()
    setErr(null)
    setMsg(null)
    try{
      await api.put(`/api/invitation-codes/${editId}`, form)
      setEditId(null)
      setForm({ code:'', eventId:'', maxUses:''})
      setMsg('Código actualizado')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const deleteCode = async (id) => {
    if(!confirm('¿Eliminar este código?')) return
    setErr(null)
    setMsg(null)
    try{
      await api.delete(`/api/invitation-codes/${id}`)
      setMsg('Código eliminado')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const editCode = (code) => {
    setEditId(code.id || code._id)
    setForm({
      code: code.code || '',
      eventId: code.eventId || '',
      maxUses: code.maxUses || ''
    })
    setErr(null)
    setMsg(null)
  }

  const cancelEdit = () => {
    setEditId(null)
    setForm({ code:'', eventId:'', maxUses:''})
    setErr(null)
    setMsg(null)
  }

  const filtered = list.filter(c => 
    !search || (c.code||'').toLowerCase().includes(search.toLowerCase())
  )

  const getEventName = (eventId) => {
    const event = events.find(e => (e.id || e._id) === eventId)
    return event ? event.title : eventId
  }

  return (
    <div className="grid lg:grid-cols-2 gap-6">
      <div className="card">
        <h2 className="text-xl font-semibold mb-3">{editId ? 'Editar' : 'Crear'} Código de Invitación</h2>
        <form onSubmit={editId ? update : create} className="grid gap-3">
          <div className="flex gap-2">
            <input className="input flex-1" placeholder="Código *" required value={form.code} onChange={e=>setForm({...form, code:e.target.value})} />
            <button type="button" onClick={generateCode} className="btn bg-neutral-200">🎲 Generar</button>
          </div>
          <select className="input" required value={form.eventId} onChange={e=>setForm({...form, eventId:e.target.value})}>
            <option value="">Seleccionar Evento *</option>
            {events.map((ev,i)=>(
              <option key={i} value={ev.id || ev._id}>{ev.title}</option>
            ))}
          </select>
          <input className="input" placeholder="Usos máximos (opcional)" type="number" value={form.maxUses} onChange={e=>setForm({...form, maxUses:e.target.value})} />
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
          <h2 className="text-xl font-semibold">Códigos de Invitación ({filtered.length})</h2>
          <button onClick={load} className="btn bg-neutral-100 text-sm">↻ Recargar</button>
        </div>
        <input 
          className="input mb-3 w-full" 
          placeholder="Buscar por código..." 
          value={search} 
          onChange={e=>setSearch(e.target.value)} 
        />
        <div className="space-y-2 max-h-[600px] overflow-y-auto">
          {filtered.length === 0 ? (
            <div className="text-neutral-400 text-center py-8">No hay códigos</div>
          ) : (
            filtered.map((c,idx)=>(
              <div key={idx} className="p-4 rounded-xl border hover:shadow-md transition">
                <div className="flex items-start justify-between mb-2">
                  <div className="flex-1">
                    <div className="font-mono font-semibold text-lg">{c.code}</div>
                    <div className="text-sm text-neutral-600 mt-1">
                      🎫 Evento: {getEventName(c.eventId)}
                    </div>
                    {c.maxUses && (
                      <div className="text-xs text-neutral-500 mt-1">Usos máximos: {c.maxUses}</div>
                    )}
                    {c.used !== undefined && (
                      <div className="text-xs text-neutral-500">Usado: {c.used} veces</div>
                    )}
                  </div>
                </div>
                <div className="flex gap-2 mt-3">
                  <button onClick={()=>editCode(c)} className="btn bg-blue-100 text-blue-700 text-sm">Editar</button>
                  <button onClick={()=>deleteCode(c.id || c._id)} className="btn bg-red-100 text-red-700 text-sm">Eliminar</button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  )
}
