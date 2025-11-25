package com.example.demo.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;

import com.example.demo.config.Database;
import com.example.demo.config.MongoDatabaseConfig;
import com.example.demo.dao.EventDao;
import com.example.demo.dao.InvitationCodeDao;
import com.example.demo.dao.RegistrationDao;
import com.example.demo.model.Event;
import com.example.demo.model.InvitationCode;
import com.example.demo.model.Registration;
import com.example.demo.model.RegistrationStatus;

public class RegistrationService {
    private final RegistrationDao regDao = new RegistrationDao();
    private final EventDao eventDao = new EventDao();
    private final InvitationCodeDao codeDao = new InvitationCodeDao();

    public Registration create(Long eventId, Long attendeeId, String invitationCode, Double paidAmount) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            return createWithMongo(eventId, attendeeId, invitationCode, paidAmount);
        }
        try (Connection c = Database.get()) {
            c.setAutoCommit(false);
            try {
                Event e = eventDao.findById(eventId);
                if (e == null || !e.isActive()) throw new IllegalArgumentException("Evento no disponible");
                if (invitationCode != null && !invitationCode.isBlank()) {
                    InvitationCode ic = codeDao.find(invitationCode);
                    if (ic == null || !ic.getEventId().equals(eventId))
                        throw new IllegalArgumentException("Código de invitación inválido");
                    if (ic.getExpiresAt() != null && ic.getExpiresAt().isBefore(Instant.now()))
                        throw new IllegalArgumentException("Código de invitación expirado");
                    if (ic.getUsed() >= ic.getMaxUses())
                        throw new IllegalArgumentException("Código de invitación sin cupos");
                }
                int confirmed = regDao.countConfirmed(eventId, c);
                Registration r = new Registration();
                r.setEventId(eventId);
                r.setAttendeeId(attendeeId);
                r.setPaidAmount(paidAmount);
                r.setStatus(confirmed < e.getCapacity() ? RegistrationStatus.CONFIRMED : RegistrationStatus.WAITLISTED);
                r = regDao.insert(r, c);
                if (invitationCode != null && !invitationCode.isBlank()) { codeDao.incrementUse(invitationCode, c); }
                c.commit();
                return r;
            } catch (Exception ex) {
                c.rollback();
                if (ex instanceof SQLException) throw (SQLException) ex;
                throw new SQLException(ex);
            } finally { c.setAutoCommit(true); }
        }
    }
    public void cancelAndPromote(Long registrationId, Long eventId) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            cancelAndPromoteMongo(registrationId, eventId);
            return;
        }
        try (Connection c = Database.get()) {
            c.setAutoCommit(false);
            try {
                regDao.cancel(registrationId, c);
                Registration next = regDao.findNextWaitlisted(eventId, c);
                if (next != null) { regDao.setStatus(next.getId(), RegistrationStatus.CONFIRMED, c); }
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                if (ex instanceof SQLException) throw (SQLException) ex;
                throw new SQLException(ex);
            } finally { c.setAutoCommit(true); }
        }
    }

    private Registration createWithMongo(Long eventId, Long attendeeId, String invitationCode, Double paidAmount) throws SQLException {
        try {
            Event e = eventDao.findById(eventId);
            if (e == null || !e.isActive()) throw new IllegalArgumentException("Evento no disponible");
            InvitationCode ic = null;
            if (invitationCode != null && !invitationCode.isBlank()) {
                ic = codeDao.find(invitationCode);
                if (ic == null || !ic.getEventId().equals(eventId))
                    throw new IllegalArgumentException("Código de invitación inválido");
                if (ic.getExpiresAt() != null && ic.getExpiresAt().isBefore(Instant.now()))
                    throw new IllegalArgumentException("Código de invitación expirado");
                if (ic.getUsed() >= ic.getMaxUses())
                    throw new IllegalArgumentException("Código de invitación sin cupos");
            }
            int confirmed = regDao.countConfirmed(eventId, null);
            Registration r = new Registration();
            r.setEventId(eventId);
            r.setAttendeeId(attendeeId);
            r.setPaidAmount(paidAmount);
            r.setStatus(confirmed < e.getCapacity() ? RegistrationStatus.CONFIRMED : RegistrationStatus.WAITLISTED);
            r = regDao.insert(r, null);
            if (ic != null) {
                codeDao.incrementUse(invitationCode, null);
            }
            return r;
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    private void cancelAndPromoteMongo(Long registrationId, Long eventId) throws SQLException {
        try {
            regDao.cancel(registrationId, null);
            Registration next = regDao.findNextWaitlisted(eventId, null);
            if (next != null) {
                regDao.setStatus(next.getId(), RegistrationStatus.CONFIRMED, null);
            }
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }
}
