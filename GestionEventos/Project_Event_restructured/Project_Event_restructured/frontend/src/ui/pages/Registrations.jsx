import React, { useEffect, useState } from 'react'
import { api } from '../../api'

export default function Registrations(){
  const [list,setList] = useState([])
  const [events,setEvents] = useState([])
  const [attendees,setAttendees] = useState([])
  const [form,setForm] = useState({ attendeeId:'', eventId:'', status:'PENDING' })
  const [editId, setEditId] = useState(null)
  const [search, setSearch] = useState('')
  const [filterStatus, setFilterStatus] = useState('ALL')
  const [err,setErr] = useState(null)
  const [msg,setMsg] = useState(null)

  const load = async () => {
    try{ 
      const {data} = await api.get('/api/registrations')
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

  const loadAttendees = async () => {
    try{
      const {data} = await api.get('/api/attendees')
      setAttendees(data?.items || data || [])
    }catch(e){ console.error(e) }
  }

  useEffect(()=>{ 
    load()
    loadEvents()
    loadAttendees()
  }, [])

  const create = async (e) => {
    e.preventDefault()
    setErr(null)
    setMsg(null)
    try{ 
      await api.post('/api/registrations', form)
      setForm({ attendeeId:'', eventId:'', status:'PENDING'})
      setMsg('Inscripción creada exitosamente')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const update = async (e) => {
    e.preventDefault()
    setErr(null)
    setMsg(null)
    try{
      await api.put(`/api/registrations/${editId}`, form)
      setEditId(null)
      setForm({ attendeeId:'', eventId:'', status:'PENDING'})
      setMsg('Inscripción actualizada')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const deleteRegistration = async (id) => {
    if(!confirm('¿Eliminar esta inscripción?')) return
    setErr(null)
    setMsg(null)
    try{
      await api.delete(`/api/registrations/${id}`)
      setMsg('Inscripción eliminada')
      load()
    }catch(e){ setErr(e?.response?.data?.message || e.message) }
  }

  const editRegistration = (reg) => {
    setEditId(reg.id || reg._id)
    setForm({
      attendeeId: reg.attendeeId || '',
      eventId: reg.eventId || '',
      status: reg.status || 'PENDING'
    })
    setErr(null)
    setMsg(null)
  }

  const cancelEdit = () => {
    setEditId(null)
    setForm({ attendeeId:'', eventId:'', status:'PENDING'})
    setErr(null)
    setMsg(null)
  }

  const getEventName = (eventId) => {
    const event = events.find(e => (e.id || e._id) === eventId)
    return event ? event.title : eventId
  }

  const getAttendeeName = (attendeeId) => {
    const attendee = attendees.find(a => (a.id || a._id) === attendeeId)
    return attendee ? attendee.name : attendeeId
  }

  const filtered = list.filter(r => {
    const matchesSearch = !search || 
      getAttendeeName(r.attendeeId).toLowerCase().includes(search.toLowerCase()) ||
      getEventName(r.eventId).toLowerCase().includes(search.toLowerCase())
    const matchesStatus = filterStatus === 'ALL' || r.status === filterStatus
    return matchesSearch && matchesStatus
  })

  const getStatusColor = (status) => {
    switch(status) {
      case 'CONFIRMED': return 'bg-green-100 text-green-700'
      case 'PENDING': return 'bg-yellow-100 text-yellow-700'
      case 'CANCELLED': return 'bg-red-100 text-red-700'
      default: return 'bg-neutral-100 text-neutral-700'
    }
  }

  return (
    <div className="grid lg:grid-cols-2 gap-6">
      <div className="card">
        <h2 className="text-xl font-semibold mb-3">{editId ? 'Editar' : 'Nueva'} Inscripción</h2>
        <form onSubmit={editId ? update : create} className="grid gap-3">
          <select className="input" required value={form.attendeeId} onChange={e=>setForm({...form, attendeeId:e.target.value})}>
            <option value="">Seleccionar Asistente *</option>
            {attendees.map((a,i)=>(
              <option key={i} value={a.id || a._id}>{a.name} - {a.email}</option>
            ))}
          </select>
          <select className="input" required value={form.eventId} onChange={e=>setForm({...form, eventId:e.target.value})}>
            <option value="">Seleccionar Evento *</option>
            {events.map((ev,i)=>(
              <option key={i} value={ev.id || ev._id}>{ev.title}</option>
            ))}
          </select>
          <select className="input" value={form.status} onChange={e=>setForm({...form, status:e.target.value})}>
            <option value="PENDING">Pendiente</option>
            <option value="CONFIRMED">Confirmada</option>
            <option value="CANCELLED">Cancelada</option>
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
          <h2 className="text-xl font-semibold">Inscripciones ({filtered.length})</h2>
          <button onClick={load} className="btn bg-neutral-100 text-sm">↻ Recargar</button>
        </div>
        <input 
          className="input mb-2 w-full" 
          placeholder="Buscar por asistente o evento..." 
          value={search} 
          onChange={e=>setSearch(e.target.value)} 
        />
        <select className="input mb-3 w-full" value={filterStatus} onChange={e=>setFilterStatus(e.target.value)}>
          <option value="ALL">Todos los estados</option>
          <option value="PENDING">Pendiente</option>
          <option value="CONFIRMED">Confirmada</option>
          <option value="CANCELLED">Cancelada</option>
        </select>
        <div className="space-y-2 max-h-[550px] overflow-y-auto">
          {filtered.length === 0 ? (
            <div className="text-neutral-400 text-center py-8">No hay inscripciones</div>
          ) : (
            filtered.map((r,idx)=>(
              <div key={idx} className="p-4 rounded-xl border hover:shadow-md transition">
                <div className="flex items-start justify-between mb-2">
                  <div className="flex-1">
                    <div className="font-semibold">👤 {getAttendeeName(r.attendeeId)}</div>
                    <div className="text-sm text-neutral-600 mt-1">
                      🎫 {getEventName(r.eventId)}
                    </div>
                    <div className="mt-2">
                      <span className={`text-xs px-2 py-1 rounded ${getStatusColor(r.status)}`}>
                        {r.status || 'PENDING'}
                      </span>
                    </div>
                    {r.registrationDate && (
                      <div className="text-xs text-neutral-500 mt-1">
                        Fecha: {new Date(r.registrationDate).toLocaleDateString()}
                      </div>
                    )}
                  </div>
                </div>
                <div className="flex gap-2 mt-3">
                  <button onClick={()=>editRegistration(r)} className="btn bg-blue-100 text-blue-700 text-sm">Editar</button>
                  <button onClick={()=>deleteRegistration(r.id || r._id)} className="btn bg-red-100 text-red-700 text-sm">Eliminar</button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  )
}
