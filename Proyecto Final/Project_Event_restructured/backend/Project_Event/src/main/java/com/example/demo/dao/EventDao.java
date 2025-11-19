package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.example.demo.config.Database;
import com.example.demo.config.MongoDatabaseConfig;
import com.example.demo.model.Event;
import com.example.demo.model.LocationType;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

public class EventDao {
    public Event insert(Event e) throws SQLException {
        Instant now = Instant.now();
        e.setCreatedAt(now); e.setUpdatedAt(now); e.setActive(true);
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                long id = MongoDatabaseConfig.nextId("events");
                e.setId(id);
                events().insertOne(toDoc(e));
                return e;
            } catch (Exception ex) {
                throw wrap(ex);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO events(title,description,category,capacity,start_at,end_at,location_type,address,link,room,active,created_at,updated_at) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getTitle());
            ps.setString(2, e.getDescription());
            ps.setString(3, e.getCategory());
            ps.setInt(4, e.getCapacity());
            ps.setString(5, e.getStartAt().toString());
            ps.setString(6, e.getEndAt().toString());
            ps.setString(7, e.getLocationType().name());
            ps.setString(8, e.getAddress());
            ps.setString(9, e.getLink());
            ps.setString(10, e.getRoom());
            ps.setInt(11, e.isActive()?1:0);
            ps.setString(12, e.getCreatedAt().toString());
            ps.setString(13, e.getUpdatedAt().toString());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setId(keys.getLong(1));
            }
            return e;
        }
    }

    public void update(Long id, Event e) throws SQLException {
        e.setUpdatedAt(Instant.now());
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                events().updateOne(Filters.eq("_id", id), Updates.combine(
                        Updates.set("title", e.getTitle()),
                        Updates.set("description", e.getDescription()),
                        Updates.set("category", e.getCategory()),
                        Updates.set("capacity", e.getCapacity()),
                        Updates.set("start_at", e.getStartAt().toString()),
                        Updates.set("end_at", e.getEndAt().toString()),
                        Updates.set("location_type", e.getLocationType().name()),
                        Updates.set("address", e.getAddress()),
                        Updates.set("link", e.getLink()),
                        Updates.set("room", e.getRoom()),
                        Updates.set("updated_at", e.getUpdatedAt().toString())));
                return;
            } catch (Exception ex) {
                throw wrap(ex);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE events SET title=?,description=?,category=?,capacity=?,start_at=?,end_at=?,location_type=?,address=?,link=?,room=?,updated_at=? WHERE id=?")) {
            ps.setString(1, e.getTitle());
            ps.setString(2, e.getDescription());
            ps.setString(3, e.getCategory());
            ps.setInt(4, e.getCapacity());
            ps.setString(5, e.getStartAt().toString());
            ps.setString(6, e.getEndAt().toString());
            ps.setString(7, e.getLocationType().name());
            ps.setString(8, e.getAddress());
            ps.setString(9, e.getLink());
            ps.setString(10, e.getRoom());
            ps.setString(11, e.getUpdatedAt().toString());
            ps.setLong(12, id);
            ps.executeUpdate();
        }
    }

    public void setActive(Long id, boolean active) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                events().updateOne(Filters.eq("_id", id), Updates.combine(
                        Updates.set("active", active),
                        Updates.set("updated_at", Instant.now().toString())));
                return;
            } catch (Exception ex) {
                throw wrap(ex);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("UPDATE events SET active=?, updated_at=? WHERE id=?")) {
            ps.setInt(1, active?1:0);
            ps.setString(2, Instant.now().toString());
            ps.setLong(3, id);
            ps.executeUpdate();
        }
    }

    public List<Event> listAll() throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                List<Event> out = new ArrayList<>();
                for (Document doc : events().find().sort(Sorts.descending("start_at"))) {
                    out.add(map(doc));
                }
                return out;
            } catch (Exception ex) {
                throw wrap(ex);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM events ORDER BY start_at DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Event> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public Event findById(Long id) throws SQLException {
        if (MongoDatabaseConfig.isEnabled()) {
            try {
                Document doc = events().find(Filters.eq("_id", id)).first();
                return doc == null ? null : map(doc);
            } catch (Exception ex) {
                throw wrap(ex);
            }
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM events WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    private MongoCollection<Document> events() {
        return MongoDatabaseConfig.getDb().getCollection("events");
    }

    private Event map(ResultSet rs) throws SQLException {
        Event e = new Event();
        e.setId(rs.getLong("id"));
        e.setTitle(rs.getString("title"));
        e.setDescription(rs.getString("description"));
        e.setCategory(rs.getString("category"));
        e.setCapacity(rs.getInt("capacity"));
        e.setStartAt(Instant.parse(rs.getString("start_at")));
        e.setEndAt(Instant.parse(rs.getString("end_at")));
        e.setLocationType(LocationType.valueOf(rs.getString("location_type")));
        e.setAddress(rs.getString("address"));
        e.setLink(rs.getString("link"));
        e.setRoom(rs.getString("room"));
        e.setActive(rs.getInt("active") == 1);
        e.setCreatedAt(Instant.parse(rs.getString("created_at")));
        e.setUpdatedAt(Instant.parse(rs.getString("updated_at")));
        return e;
    }

    private Event map(Document doc) {
        Event e = new Event();
        e.setId(doc.getLong("_id"));
        e.setTitle(doc.getString("title"));
        e.setDescription(doc.getString("description"));
        e.setCategory(doc.getString("category"));
        e.setCapacity(doc.getInteger("capacity", 0));
        e.setStartAt(Instant.parse(doc.getString("start_at")));
        e.setEndAt(Instant.parse(doc.getString("end_at")));
        e.setLocationType(LocationType.valueOf(doc.getString("location_type")));
        e.setAddress(doc.getString("address"));
        e.setLink(doc.getString("link"));
        e.setRoom(doc.getString("room"));
        e.setActive(Boolean.TRUE.equals(doc.getBoolean("active")));
        e.setCreatedAt(Instant.parse(doc.getString("created_at")));
        e.setUpdatedAt(Instant.parse(doc.getString("updated_at")));
        return e;
    }

    private Document toDoc(Event e) {
        return new Document("_id", e.getId())
                .append("title", e.getTitle())
                .append("description", e.getDescription())
                .append("category", e.getCategory())
                .append("capacity", e.getCapacity())
                .append("start_at", e.getStartAt().toString())
                .append("end_at", e.getEndAt().toString())
                .append("location_type", e.getLocationType().name())
                .append("address", e.getAddress())
                .append("link", e.getLink())
                .append("room", e.getRoom())
                .append("active", e.isActive())
                .append("created_at", e.getCreatedAt().toString())
                .append("updated_at", e.getUpdatedAt().toString());
    }

    private SQLException wrap(Exception e) {
        return e instanceof SQLException ? (SQLException) e : new SQLException(e);
    }
}
