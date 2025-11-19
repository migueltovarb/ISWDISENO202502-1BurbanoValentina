package com.example.demo.service;

import com.example.demo.dao.AttendeeDao;
import com.example.demo.model.Attendee;

import java.sql.SQLException;

public class AttendeeService {
    private final AttendeeDao dao = new AttendeeDao();
    public Attendee create(Attendee a) throws SQLException {
        if (a.getName()==null || a.getName().isBlank()) throw new IllegalArgumentException("Nombre requerido");
        if (a.getDocumentId()==null || a.getDocumentId().isBlank()) throw new IllegalArgumentException("Documento requerido");
        if (a.getEmail()==null || !a.getEmail().contains("@")) throw new IllegalArgumentException("Email inválido");
        return dao.insert(a);
    }
    public void update(Long id, Attendee a) throws SQLException { dao.update(id, a); }
    public Attendee find(Long id) throws SQLException { return dao.findById(id); }
}
