package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

import org.bson.Document;

import com.example.demo.config.Database;
import com.example.demo.config.MongoDatabaseConfig;
import com.example.demo.model.Attendee;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class AttendeeDao {
    public Attendee insert(Attendee a) throws SQLException {
        Instant now = Instant.now();
        a.setCreatedAt(now); a.setUpdatedAt(now);
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                long id = MongoDatabaseConfig.nextId("attendees");
                a.setId(id);
                attendees().insertOne(toDoc(a));
                return a;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO attendees(name,document_id,email,phone,email_notifications,created_at,updated_at) VALUES(?,?,?,?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getName());
            ps.setString(2, a.getDocumentId());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getPhone());
            ps.setInt(5, a.isEmailNotifications()?1:0);
            ps.setString(6, a.getCreatedAt().toString());
            ps.setString(7, a.getUpdatedAt().toString());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) a.setId(keys.getLong(1));
            }
            return a;
        }
    }

    public void update(Long id, Attendee a) throws SQLException {
        a.setUpdatedAt(Instant.now());
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                attendees().updateOne(Filters.eq("_id", id), Updates.combine(
                        Updates.set("name", a.getName()),
                        Updates.set("document_id", a.getDocumentId()),
                        Updates.set("email", a.getEmail()),
                        Updates.set("phone", a.getPhone()),
                        Updates.set("email_notifications", a.isEmailNotifications()),
                        Updates.set("updated_at", a.getUpdatedAt().toString())));
                return;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE attendees SET name=?, document_id=?, email=?, phone=?, email_notifications=?, updated_at=? WHERE id=?")) {
            ps.setString(1, a.getName());
            ps.setString(2, a.getDocumentId());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getPhone());
            ps.setInt(5, a.isEmailNotifications()?1:0);
            ps.setString(6, a.getUpdatedAt().toString());
            ps.setLong(7, id);
            ps.executeUpdate();
        }
    }

    public Attendee findById(Long id) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                Document doc = attendees().find(Filters.eq("_id", id)).first();
                return doc == null ? null : map(doc);
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM attendees WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    private MongoCollection<Document> attendees() {
        return MongoDatabaseConfig.getDb().getCollection("attendees");
    }

    private Attendee map(ResultSet rs) throws SQLException {
        Attendee a = new Attendee();
        a.setId(rs.getLong("id"));
        a.setName(rs.getString("name"));
        a.setDocumentId(rs.getString("document_id"));
        a.setEmail(rs.getString("email"));
        a.setPhone(rs.getString("phone"));
        a.setEmailNotifications(rs.getInt("email_notifications")==1);
        a.setCreatedAt(Instant.parse(rs.getString("created_at")));
        a.setUpdatedAt(Instant.parse(rs.getString("updated_at")));
        return a;
    }

    private Attendee map(Document doc) {
        Attendee a = new Attendee();
        a.setId(doc.getLong("_id"));
        a.setName(doc.getString("name"));
        a.setDocumentId(doc.getString("document_id"));
        a.setEmail(doc.getString("email"));
        a.setPhone(doc.getString("phone"));
        a.setEmailNotifications(Boolean.TRUE.equals(doc.getBoolean("email_notifications")));
        a.setCreatedAt(Instant.parse(doc.getString("created_at")));
        a.setUpdatedAt(Instant.parse(doc.getString("updated_at")));
        return a;
    }

    private Document toDoc(Attendee a) {
        return new Document("_id", a.getId())
                .append("name", a.getName())
                .append("document_id", a.getDocumentId())
                .append("email", a.getEmail())
                .append("phone", a.getPhone())
                .append("email_notifications", a.isEmailNotifications())
                .append("created_at", a.getCreatedAt().toString())
                .append("updated_at", a.getUpdatedAt().toString());
    }

    private SQLException wrap(Exception e) {
        return e instanceof SQLException ? (SQLException) e : new SQLException(e);
    }
}
