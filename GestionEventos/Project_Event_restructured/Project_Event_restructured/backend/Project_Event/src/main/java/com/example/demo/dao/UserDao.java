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
import com.example.demo.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class UserDao {
    public User findByEmail(String email) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                Document doc = users().find(Filters.eq("email", email)).first();
                return doc == null ? null : map(doc);
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public User findById(Long id) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                Document doc = users().find(Filters.eq("_id", id)).first();
                return doc == null ? null : map(doc);
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public User insert(User u) throws SQLException {
        Instant now = Instant.now();
        u.setCreatedAt(now); u.setUpdatedAt(now);
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                long id = MongoDatabaseConfig.nextId("users");
                u.setId(id);
                Document doc = new Document("_id", id)
                        .append("name", u.getName())
                        .append("email", u.getEmail())
                        .append("password_hash", u.getPasswordHash())
                        .append("active", u.isActive())
                        .append("created_at", u.getCreatedAt().toString())
                        .append("updated_at", u.getUpdatedAt().toString());
                users().insertOne(doc);
                return u;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO users(name,email,password_hash,active,created_at,updated_at) VALUES(?,?,?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setInt(4, u.isActive()?1:0);
            ps.setString(5, u.getCreatedAt().toString());
            ps.setString(6, u.getUpdatedAt().toString());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setId(keys.getLong(1));
            }
            return u;
        }
    }

    public void updatePassword(Long userId, String newHash) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                users().updateOne(Filters.eq("_id", userId), Updates.combine(
                        Updates.set("password_hash", newHash),
                        Updates.set("updated_at", Instant.now().toString())));
                return;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("UPDATE users SET password_hash=?, updated_at=? WHERE id=?")) {
            ps.setString(1, newHash);
            ps.setString(2, Instant.now().toString());
            ps.setLong(3, userId);
            ps.executeUpdate();
        }
    }

    private MongoCollection<Document> users() {
        return MongoDatabaseConfig.getDb().getCollection("users");
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setActive(rs.getInt("active")==1);
        u.setCreatedAt(Instant.parse(rs.getString("created_at")));
        u.setUpdatedAt(Instant.parse(rs.getString("updated_at")));
        return u;
    }

    private User map(Document doc) {
        User u = new User();
        u.setId(doc.getLong("_id"));
        u.setName(doc.getString("name"));
        u.setEmail(doc.getString("email"));
        u.setPasswordHash(doc.getString("password_hash"));
        u.setActive(Boolean.TRUE.equals(doc.getBoolean("active")));
        u.setCreatedAt(Instant.parse(doc.getString("created_at")));
        u.setUpdatedAt(Instant.parse(doc.getString("updated_at")));
        return u;
    }

    private SQLException wrap(Exception e) {
        return e instanceof SQLException ? (SQLException) e : new SQLException(e);
    }
}
