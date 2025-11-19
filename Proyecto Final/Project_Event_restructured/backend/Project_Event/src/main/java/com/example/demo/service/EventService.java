package com.example.demo.service;

import com.example.demo.dao.EventDao;
import com.example.demo.model.Event;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public class EventService {
    private final EventDao dao = new EventDao();
    public Event create(Event e) throws SQLException {
        if (e.getTitle() == null || e.getTitle().isBlank()) throw new IllegalArgumentException("Título requerido");
        if (e.getCapacity() <= 0) throw new IllegalArgumentException("Aforo debe ser mayor a 0");
        if (e.getStartAt() == null || e.getEndAt() == null || !e.getEndAt().isAfter(e.getStartAt()))
            throw new IllegalArgumentException("Rango de fechas inválido");
        return dao.insert(e);
    }
    public void update(Long id, Event e) throws SQLException {
        e.setUpdatedAt(Instant.now());
        dao.update(id, e);
    }
    public void setActive(Long id, boolean active) throws SQLException { dao.setActive(id, active); }
    public List<Event> list() throws SQLException { return dao.listAll(); }
    public Event find(Long id) throws SQLException { return dao.findById(id); }
}
