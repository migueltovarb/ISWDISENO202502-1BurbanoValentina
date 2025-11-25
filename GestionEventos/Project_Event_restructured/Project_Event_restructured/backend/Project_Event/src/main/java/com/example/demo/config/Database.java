package com.example.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {
    private static HikariDataSource ds;
    private static Properties props;

    public static void init() throws IOException, SQLException {
        props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            props.load(fis);
        } catch (IOException e) {
            try (var is = Database.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (is != null) props.load(is);
            }
        }

        String dbPath = props.getProperty("db.path", "data/events.db");
        Path p = Path.of(dbPath).getParent();
        if (p != null) Files.createDirectories(p);

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:sqlite:" + dbPath);
        cfg.setDriverClassName("org.sqlite.JDBC");
        cfg.setMaximumPoolSize(10);
        cfg.setPoolName("sqlite-pool");

        ds = new HikariDataSource(cfg);

        try (Connection c = get(); Statement st = c.createStatement()) {
            st.execute("PRAGMA foreign_keys=ON;");
        }

        createTables();
    }

    public static Connection get() throws SQLException {
        return ds.getConnection();
    }

    private static void createTables() throws SQLException {
        try (Connection c = get(); Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL UNIQUE, password_hash TEXT NOT NULL, active INTEGER NOT NULL DEFAULT 1, created_at TEXT NOT NULL, updated_at TEXT NOT NULL);");
            st.execute("CREATE TABLE IF NOT EXISTS password_reset_tokens (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE, token TEXT NOT NULL UNIQUE, expires_at TEXT NOT NULL, used INTEGER NOT NULL DEFAULT 0, created_at TEXT NOT NULL);");
            st.execute("CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, description TEXT, category TEXT, capacity INTEGER NOT NULL, start_at TEXT NOT NULL, end_at TEXT NOT NULL, location_type TEXT NOT NULL, address TEXT, link TEXT, room TEXT, active INTEGER NOT NULL DEFAULT 1, created_at TEXT NOT NULL, updated_at TEXT NOT NULL);");
            st.execute("CREATE TABLE IF NOT EXISTS attendees (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, document_id TEXT NOT NULL, email TEXT NOT NULL, phone TEXT, email_notifications INTEGER NOT NULL DEFAULT 1, created_at TEXT NOT NULL, updated_at TEXT NOT NULL);");
            st.execute("CREATE TABLE IF NOT EXISTS invitation_codes (code TEXT PRIMARY KEY, event_id INTEGER NOT NULL REFERENCES events(id) ON DELETE CASCADE, max_uses INTEGER NOT NULL DEFAULT 1, used INTEGER NOT NULL DEFAULT 0, expires_at TEXT);");
            st.execute("CREATE TABLE IF NOT EXISTS registrations (id INTEGER PRIMARY KEY AUTOINCREMENT, event_id INTEGER NOT NULL REFERENCES events(id) ON DELETE CASCADE, attendee_id INTEGER NOT NULL REFERENCES attendees(id) ON DELETE CASCADE, status TEXT NOT NULL, paid_amount REAL, created_at TEXT NOT NULL);");
        }
    }
}
