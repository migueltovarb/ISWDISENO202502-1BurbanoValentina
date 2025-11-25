package com.example.demo.email;

public interface EmailService {
    void send(String to, String subject, String bodyHtml);
}
