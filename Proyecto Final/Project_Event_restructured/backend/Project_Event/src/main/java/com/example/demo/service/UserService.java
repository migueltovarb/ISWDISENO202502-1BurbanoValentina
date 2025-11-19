package com.example.demo.service;

import com.example.demo.dao.PasswordResetTokenDao;
import com.example.demo.dao.UserDao;
import com.example.demo.email.EmailService;
import com.example.demo.email.ConsoleEmailService;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.util.Hash;

import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class UserService {
    private final UserDao userDao = new UserDao();
    private final PasswordResetTokenDao tokenDao = new PasswordResetTokenDao();
    private final EmailService email = new ConsoleEmailService();

    public User register(String name, String email, String password) throws SQLException {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Nombre requerido");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Email inválido");
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Contraseña demasiado corta");
        if (userDao.findByEmail(email) != null) throw new IllegalArgumentException("Email ya registrado");

        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPasswordHash(Hash.hashPassword(password));
        u.setActive(true);
        return userDao.insert(u);
    }
    public User login(String email, String password) throws SQLException {
        User u = userDao.findByEmail(email);
        if (u == null) throw new IllegalArgumentException("Credenciales inválidas");
        if (!u.isActive()) throw new IllegalStateException("Usuario inactivo");
        if (!Hash.verify(password, u.getPasswordHash())) throw new IllegalArgumentException("Credenciales inválidas");
        return u;
    }
    public void requestPasswordReset(String emailAddr) throws SQLException {
        User u = userDao.findByEmail(emailAddr);
        if (u == null) return;
        String token = UUID.randomUUID().toString();
        PasswordResetToken t = new PasswordResetToken();
        t.setUserId(u.getId());
        t.setToken(token);
        t.setExpiresAt(Instant.now().plus(60, ChronoUnit.MINUTES));
        t.setUsed(false);
        t.setCreatedAt(Instant.now());
        tokenDao.insert(t);
        String resetUrl = "http://localhost:7070/password-reset?token=" + token;
        String body = "<p>Hola " + u.getName() + ",</p><p>Para restablecer tu contraseña haz clic en: "
                + "<a href='" + resetUrl + "'>" + resetUrl + "</a></p>";
        email.send(u.getEmail(), "Restablecer contraseña", body);
    }
    public void resetPassword(String token, String newPassword) throws SQLException {
        var t = tokenDao.findByToken(token);
        if (t == null || t.isUsed() || t.getExpiresAt().isBefore(Instant.now()))
            throw new IllegalArgumentException("Token inválido o expirado");
        userDao.updatePassword(t.getUserId(), Hash.hashPassword(newPassword));
        tokenDao.markUsed(t.getId());
    }
}
