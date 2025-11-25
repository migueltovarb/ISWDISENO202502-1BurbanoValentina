package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.config.Database;
import com.example.demo.config.MongoDatabaseConfig;
import com.example.demo.web.Api;

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        try {
            // Allow overriding the port via program argument (first arg), system property 'app.port' or env var PORT
            if (args != null && args.length > 0 && System.getProperty("app.port") == null && System.getenv("PORT") == null) {
                System.setProperty("app.port", args[0]);
            }
            // Log effective port sources for debugging
            String sysProp = System.getProperty("app.port");
            String envPort = System.getenv("PORT");
            log.info("Startup args: {} | app.port(system property)={} | PORT(env)={}", (Object)args, sysProp, envPort);
            Database.init();
            // Inicializa conexión a MongoDB si está configurada (opcional)
            MongoDatabaseConfig.initIfConfigured();
            Api.start();
        } catch (Exception e) {
            log.error("Fatal error starting application", e);
            System.exit(1);
        }
    }
}
