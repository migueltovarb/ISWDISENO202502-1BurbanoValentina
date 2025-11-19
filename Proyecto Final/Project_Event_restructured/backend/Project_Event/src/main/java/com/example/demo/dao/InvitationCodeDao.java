package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;

import org.bson.Document;

import com.example.demo.config.Database;
import com.example.demo.config.MongoDatabaseConfig;
import com.example.demo.model.InvitationCode;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class InvitationCodeDao {
    public InvitationCode insert(InvitationCode code) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                codes().insertOne(toDoc(code));
                return code;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO invitation_codes(code, event_id, max_uses, used, expires_at) VALUES(?,?,?,?,?)")) {
            ps.setString(1, code.getCode());
            ps.setLong(2, code.getEventId());
            ps.setInt(3, code.getMaxUses());
            ps.setInt(4, code.getUsed());
            if (code.getExpiresAt() == null) ps.setNull(5, Types.VARCHAR);
            else ps.setString(5, code.getExpiresAt().toString());
            ps.executeUpdate();
            return code;
        }
    }

    public InvitationCode find(String code) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                Document doc = codes().find(Filters.eq("_id", code)).first();
                return doc == null ? null : map(doc);
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM invitation_codes WHERE code=?")) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public void incrementUse(String code, Connection c) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                codes().updateOne(Filters.eq("_id", code), Updates.inc("used", 1));
                return;
            } catch (Exception e) {
                throw wrap(e);
            }
        }
        try (PreparedStatement ps = c.prepareStatement("UPDATE invitation_codes SET used = used + 1 WHERE code=?")) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }

    private MongoCollection<Document> codes() {
        return MongoDatabaseConfig.getDb().getCollection("invitation_codes");
    }

    private InvitationCode map(ResultSet rs) throws SQLException {
        InvitationCode ic = new InvitationCode();
        ic.setCode(rs.getString("code"));
        ic.setEventId(rs.getLong("event_id"));
        ic.setMaxUses(rs.getInt("max_uses"));
        ic.setUsed(rs.getInt("used"));
        String ex = rs.getString("expires_at");
        if (ex != null) ic.setExpiresAt(Instant.parse(ex));
        return ic;
    }

    private InvitationCode map(Document doc) {
        InvitationCode ic = new InvitationCode();
        ic.setCode(doc.getString("code"));
        ic.setEventId(doc.getLong("event_id"));
        ic.setMaxUses(doc.getInteger("max_uses", 0));
        ic.setUsed(doc.getInteger("used", 0));
        String ex = doc.getString("expires_at");
        if (ex != null) ic.setExpiresAt(Instant.parse(ex));
        return ic;
    }

    private Document toDoc(InvitationCode code) {
        Document doc = new Document("_id", code.getCode())
                .append("code", code.getCode())
                .append("event_id", code.getEventId())
                .append("max_uses", code.getMaxUses())
                .append("used", code.getUsed());
        if (code.getExpiresAt() != null) {
            doc.append("expires_at", code.getExpiresAt().toString());
        }
        return doc;
    }

    private SQLException wrap(Exception e) {
        return e instanceof SQLException ? (SQLException) e : new SQLException(e);
    }
}
