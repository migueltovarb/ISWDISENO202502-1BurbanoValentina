package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.Instant;

import org.bson.Document;

import com.example.demo.config.MongoDatabaseConfig;
import com.example.demo.model.Registration;
import com.example.demo.model.RegistrationStatus;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

public class RegistrationDao {
    public Registration insert(Registration r, Connection c) throws SQLException {
        r.setCreatedAt(Instant.now());
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                long id = MongoDatabaseConfig.nextId("registrations");
                r.setId(id);
                registrations().insertOne(toDoc(r));
                return r;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO registrations(event_id, attendee_id, status, paid_amount, created_at) VALUES(?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, r.getEventId());
            ps.setLong(2, r.getAttendeeId());
            ps.setString(3, r.getStatus().name());
            if (r.getPaidAmount() == null) ps.setNull(4, Types.DOUBLE);
            else ps.setDouble(4, r.getPaidAmount());
            ps.setString(5, r.getCreatedAt().toString());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setId(keys.getLong(1));
            }
            return r;
        }
    }

    public int countConfirmed(Long eventId, Connection c) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                long count = registrations().countDocuments(Filters.and(
                        Filters.eq("event_id", eventId),
                        Filters.eq("status", RegistrationStatus.CONFIRMED.name())));
                return (int) count;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT COUNT(1) FROM registrations WHERE event_id=? AND status='CONFIRMED'")) {
            ps.setLong(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void cancel(Long registrationId, Connection c) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                registrations().updateOne(Filters.eq("_id", registrationId), Updates.set("status", RegistrationStatus.CANCELED.name()));
                return;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (PreparedStatement ps = c.prepareStatement("UPDATE registrations SET status='CANCELED' WHERE id=?")) {
            ps.setLong(1, registrationId);
            ps.executeUpdate();
        }
    }

    public Registration findNextWaitlisted(Long eventId, Connection c) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                Document doc = registrations().find(Filters.and(
                                Filters.eq("event_id", eventId),
                                Filters.eq("status", RegistrationStatus.WAITLISTED.name())))
                        .sort(Sorts.ascending("created_at"))
                        .first();
                return doc == null ? null : map(doc);
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM registrations WHERE event_id=? AND status='WAITLISTED' ORDER BY created_at ASC LIMIT 1")) {
            ps.setLong(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public void setStatus(Long regId, RegistrationStatus status, Connection c) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                registrations().updateOne(Filters.eq("_id", regId), Updates.set("status", status.name()));
                return;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (PreparedStatement ps = c.prepareStatement("UPDATE registrations SET status=? WHERE id=?")) {
            ps.setString(1, status.name());
            ps.setLong(2, regId);
            ps.executeUpdate();
        }
    }

    private MongoCollection<Document> registrations() {
        return MongoDatabaseConfig.getDb().getCollection("registrations");
    }

    private Registration map(ResultSet rs) throws SQLException {
        Registration r = new Registration();
        r.setId(rs.getLong("id"));
        r.setEventId(rs.getLong("event_id"));
        r.setAttendeeId(rs.getLong("attendee_id"));
        r.setStatus(RegistrationStatus.valueOf(rs.getString("status")));
        double v = rs.getDouble("paid_amount");
        if (!rs.wasNull()) r.setPaidAmount(v);
        r.setCreatedAt(Instant.parse(rs.getString("created_at")));
        return r;
    }

    private Registration map(Document doc) {
        Registration r = new Registration();
        r.setId(doc.getLong("_id"));
        r.setEventId(doc.getLong("event_id"));
        r.setAttendeeId(doc.getLong("attendee_id"));
        r.setStatus(RegistrationStatus.valueOf(doc.getString("status")));
        Double paid = doc.getDouble("paid_amount");
        if (paid != null) r.setPaidAmount(paid);
        r.setCreatedAt(Instant.parse(doc.getString("created_at")));
        return r;
    }

    private Document toDoc(Registration r) {
        Document doc = new Document("_id", r.getId())
                .append("event_id", r.getEventId())
                .append("attendee_id", r.getAttendeeId())
                .append("status", r.getStatus().name())
                .append("created_at", r.getCreatedAt().toString());
        if (r.getPaidAmount() != null) {
            doc.append("paid_amount", r.getPaidAmount());
        }
        return doc;
    }

    private SQLException wrap(Exception e) {
        return e instanceof SQLException ? (SQLException) e : new SQLException(e);
    }
}
