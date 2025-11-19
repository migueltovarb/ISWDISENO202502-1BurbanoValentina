package com.example.demo.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReturnDocument;

/**
 * Inicialización opcional de MongoDB. Si no se define la propiedad 'mongo.uri',
 * no se crea ningún cliente y el resto de la app continúa usando SQLite.
 */
public class MongoDatabaseConfig {
    private static final Logger log = LoggerFactory.getLogger(MongoDatabaseConfig.class);
    private static final String COUNTERS_COLLECTION = "_counters";
    private static MongoClient client;
    private static MongoDatabase db;

    public static void initIfConfigured() {
        try {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
                props.load(fis);
            } catch (IOException e) {
                try (var is = MongoDatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
                    if (is != null) props.load(is);
                }
            }

            String uri = System.getenv().getOrDefault("MONGO_URI", props.getProperty("mongo.uri", "")).trim();
            if (uri.isBlank()) {
                log.info("MongoDB no configurado (mongo.uri vacío). Se continúa sin conectar a Mongo.");
                return;
            }
            String dbName = props.getProperty("mongo.database", "eventsdb");

            ConnectionString cs = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
            client = MongoClients.create(settings);
            db = client.getDatabase(dbName);

            // Ping para verificar conectividad
            Document ping = new Document("ping", 1);
            db.runCommand(ping);
            ensureIndexes();
            log.info("Conectado a MongoDB correctamente. database={}", dbName);
        } catch (Exception e) {
            log.error("No se pudo inicializar conexión a MongoDB: {}", e.getMessage(), e);
        }
    }

    public static MongoClient getClient() { return client; }
    public static MongoDatabase getDb() { return db; }

    public static boolean isEnabled() { return db != null; }

    public static long nextId(String sequence) {
        if (db == null) throw new IllegalStateException("MongoDB no está inicializado");
        var options = new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER);
        Document doc = db.getCollection(COUNTERS_COLLECTION)
                .findOneAndUpdate(new Document("_id", sequence), new Document("$inc", new Document("seq", 1)), options);
        return doc.getLong("seq");
    }

    private static void ensureIndexes() {
        if (db == null) return;
        // Índices únicos clave para integridad similar a SQLite
        db.getCollection("users").createIndex(Indexes.ascending("email"), new IndexOptions().unique(true));
        db.getCollection("attendees").createIndex(Indexes.ascending("email"));
        db.getCollection("invitation_codes").createIndex(Indexes.ascending("code"), new IndexOptions().unique(true));
        db.getCollection("registrations").createIndex(Indexes.ascending("event_id", "status"));
    }
}
