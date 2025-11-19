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
import com.example.demo.model.PasswordResetToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class PasswordResetTokenDao {
    public PasswordResetToken insert(PasswordResetToken t) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                long id = MongoDatabaseConfig.nextId("password_reset_tokens");
                t.setId(id);
                tokens().insertOne(toDoc(t));
                return t;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO password_reset_tokens(user_id, token, expires_at, used, created_at) VALUES(?,?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, t.getUserId());
            ps.setString(2, t.getToken());
            ps.setString(3, t.getExpiresAt().toString());
            ps.setInt(4, t.isUsed() ? 1 : 0);
            ps.setString(5, t.getCreatedAt().toString());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) t.setId(keys.getLong(1));
            }
            return t;
        }
    }

    public PasswordResetToken findByToken(String token) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                Document doc = tokens().find(Filters.eq("token", token)).first();
                return doc == null ? null : map(doc);
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM password_reset_tokens WHERE token=?")) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public void markUsed(Long id) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                tokens().updateOne(Filters.eq("_id", id), Updates.set("used", true));
                return;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("UPDATE password_reset_tokens SET used=1 WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private MongoCollection<Document> tokens() {
        return MongoDatabaseConfig.getDb().getCollection("password_reset_tokens");
    }

    private PasswordResetToken map(ResultSet rs) throws SQLException {
        PasswordResetToken t = new PasswordResetToken();
        t.setId(rs.getLong("id"));
        t.setUserId(rs.getLong("user_id"));
        t.setToken(rs.getString("token"));
        t.setExpiresAt(Instant.parse(rs.getString("expires_at")));
        t.setUsed(rs.getInt("used")==1);
        t.setCreatedAt(Instant.parse(rs.getString("created_at")));
        return t;
    }

    private PasswordResetToken map(Document doc) {
        PasswordResetToken t = new PasswordResetToken();
        t.setId(doc.getLong("_id"));
        t.setUserId(doc.getLong("user_id"));
        t.setToken(doc.getString("token"));
        t.setExpiresAt(Instant.parse(doc.getString("expires_at")));
        t.setUsed(Boolean.TRUE.equals(doc.getBoolean("used")));
        t.setCreatedAt(Instant.parse(doc.getString("created_at")));
        return t;
    }

    private Document toDoc(PasswordResetToken t) {
        return new Document("_id", t.getId())
                .append("user_id", t.getUserId())
                .append("token", t.getToken())
                .append("expires_at", t.getExpiresAt().toString())
                .append("used", t.isUsed())
                .append("created_at", t.getCreatedAt().toString());
    }

    private SQLException wrap(Exception e) {
        return e instanceof SQLException ? (SQLException) e : new SQLException(e);
    }
}
