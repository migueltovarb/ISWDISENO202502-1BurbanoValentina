package com.example.demo.web;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.model.Attendee;
import com.example.demo.model.Event;
import com.example.demo.model.InvitationCode;
import com.example.demo.model.Registration;
import com.example.demo.service.AttendeeService;
import com.example.demo.service.EventService;
import com.example.demo.service.RegistrationService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Api {
    private static final ObjectMapper M = new ObjectMapper().findAndRegisterModules();
    private static final Logger log = LoggerFactory.getLogger(Api.class);

    public static void start() {
    // Allow configuring the port via env var PORT or system property 'app.port', default 7070
    String portStr = System.getenv().getOrDefault("PORT", System.getProperty("app.port", "7070"));
    int port = 7070;
    try { port = Integer.parseInt(portStr.trim()); } catch (Exception ignored) {}
    log.info("Api starting on port={}", port);
    Javalin app = Javalin.create(conf -> { conf.http.defaultContentType = "application/json"; }).start(port);

        
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", System.getProperty("CORS_ORIGIN", "*"));
            ctx.header("Access-Control-Allow-Credentials", "true");
            ctx.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        });
        app.options("/*", ctx -> {
            ctx.header("Access-Control-Allow-Origin", System.getProperty("CORS_ORIGIN", "*"));
            ctx.status(204);
        });
        UserService users = new UserService();
        EventService events = new EventService();
        AttendeeService attendees = new AttendeeService();
        RegistrationService regs = new RegistrationService();
        
        app.get("/health", ctx -> { ctx.json(java.util.Map.of("status","ok","ts", java.time.Instant.now().toString())); });
        

        app.post("/api/users/register", ctx -> {
            Map in = M.readValue(ctx.body(), Map.class);
            String name = (String) in.get("name");
            String email = (String) in.get("email");
            String password = (String) in.get("password");
            try { var u = users.register(name, email, password); ctx.json(Map.of("ok", true, "userId", u.getId())); }
            catch (Exception e) { bad(ctx, e); }
        });

        app.post("/api/users/login", ctx -> {
            Map in = M.readValue(ctx.body(), Map.class);
            String email = (String) in.get("email");
            String password = (String) in.get("password");
            try { var u = users.login(email, password); ctx.json(Map.of("ok", true, "user", Map.of("id", u.getId(), "name", u.getName(), "email", u.getEmail()))); }
            catch (Exception e) { bad(ctx, e); }
        });

        app.post("/api/users/password/request-reset", ctx -> {
            Map in = M.readValue(ctx.body(), Map.class);
            String email = (String) in.get("email");
            try { users.requestPasswordReset(email); ctx.json(Map.of("ok", true, "message", "Si el email existe, se envió un enlace de recuperación.")); }
            catch (Exception e) { bad(ctx, e); }
        });

        app.post("/api/users/password/reset", ctx -> {
            if ("application/x-www-form-urlencoded".equalsIgnoreCase(ctx.contentType())) {
                String token = ctx.formParam("token"); String newPassword = ctx.formParam("newPassword");
                try { users.resetPassword(token, newPassword); ctx.result("Contraseña actualizada. Puedes cerrar esta pestaña."); }
                catch (Exception e) { ctx.status(400).result("Error: " + e.getMessage()); }
                return;
            }
            Map in = M.readValue(ctx.body(), Map.class);
            String token = (String) in.get("token");
            String newPassword = (String) in.get("newPassword");
            try { users.resetPassword(token, newPassword); ctx.json(Map.of("ok", true)); }
            catch (Exception e) { bad(ctx, e); }
        });

        app.get("/password-reset", ctx -> {
            String token = ctx.queryParam("token");
            if (token == null) token = "";
            String html = "<html><body><h3>Restablecer contraseña</h3><form method='POST' action='/api/users/password/reset'>" +
                          "<input type='hidden' name='token' value='" + token + "'/>" +
                          "<label>Nueva contraseña:</label><input type='password' name='newPassword'/>" +
                          "<button type='submit'>Cambiar</button></form></body></html>";
            ctx.contentType("text/html").result(html);
        });

        app.post("/api/events", ctx -> {
            try { Event e = M.readValue(ctx.body(), Event.class); e = events.create(e); ctx.json(Map.of("ok", true, "id", e.getId())); }
            catch (Exception e) { bad(ctx, e); }
        });

        app.put("/api/events/{id}", ctx -> {
            try { Long id = Long.valueOf(ctx.pathParam("id")); Event e = M.readValue(ctx.body(), Event.class); events.update(id, e); ctx.json(Map.of("ok", true)); }
            catch (Exception e) { bad(ctx, e); }
        });

        app.patch("/api/events/{id}/activate", ctx -> { try { Long id = Long.valueOf(ctx.pathParam("id")); events.setActive(id, true); ctx.json(Map.of("ok", true)); } catch (Exception e) { bad(ctx, e); } });
        app.patch("/api/events/{id}/deactivate", ctx -> { try { Long id = Long.valueOf(ctx.pathParam("id")); events.setActive(id, false); ctx.json(Map.of("ok", true)); } catch (Exception e) { bad(ctx, e); } });

        app.get("/api/events", ctx -> { try { List<Event> list = events.list(); ctx.json(Map.of("ok", true, "items", list)); } catch (Exception e) { bad(ctx, e); } });

        app.post("/api/attendees", ctx -> { try { Attendee a = M.readValue(ctx.body(), Attendee.class); a = attendees.create(a); ctx.json(Map.of("ok", true, "id", a.getId())); } catch (Exception e) { bad(ctx, e); } });
        app.put("/api/attendees/{id}", ctx -> { try { Long id = Long.valueOf(ctx.pathParam("id")); Attendee a = M.readValue(ctx.body(), Attendee.class); attendees.update(id, a); ctx.json(Map.of("ok", true)); } catch (Exception e) { bad(ctx, e); } });

        app.post("/api/registrations", ctx -> { try { Map in = M.readValue(ctx.body(), Map.class);
            Long eventId = in.get("eventId")==null?null:Long.valueOf(in.get("eventId").toString());
            Long attendeeId = in.get("attendeeId")==null?null:Long.valueOf(in.get("attendeeId").toString());
            String code = (String) in.getOrDefault("invitationCode", null);
            Double paid = in.get("paidAmount")==null?null:Double.valueOf(in.get("paidAmount").toString());
            Registration r = regs.create(eventId, attendeeId, code, paid);
            ctx.json(Map.of("ok", true, "id", r.getId(), "status", r.getStatus().name())); } catch (Exception e) { bad(ctx, e); } });

        app.post("/api/registrations/{id}/cancel", ctx -> { try { Long id = Long.valueOf(ctx.pathParam("id")); Long eventId = Long.valueOf(ctx.queryParam("eventId")); regs.cancelAndPromote(id, eventId); ctx.json(Map.of("ok", true)); } catch (Exception e) { bad(ctx, e); } });

        app.post("/api/invitation-codes", ctx -> { try { Map in = M.readValue(ctx.body(), Map.class); InvitationCode ic = new InvitationCode();
            ic.setCode((String) in.get("code")); ic.setEventId(Long.valueOf(in.get("eventId").toString()));
            ic.setMaxUses(Integer.parseInt(in.get("maxUses").toString())); ic.setUsed(0);
            String ex = (String) in.getOrDefault("expiresAt", null); if (ex != null && !ex.isBlank()) ic.setExpiresAt(java.time.Instant.parse(ex));
            new com.example.demo.dao.InvitationCodeDao().insert(ic); ctx.json(Map.of("ok", true)); } catch (Exception e) { bad(ctx, e); } });

        app.get("/", ctx -> ctx.json(Map.of("status", "ok", "ts", Instant.now().toString())));
    }
    private static void bad(Context ctx, Exception e) { ctx.status(400).json(Map.of("ok", false, "error", e.getMessage())); }
}
