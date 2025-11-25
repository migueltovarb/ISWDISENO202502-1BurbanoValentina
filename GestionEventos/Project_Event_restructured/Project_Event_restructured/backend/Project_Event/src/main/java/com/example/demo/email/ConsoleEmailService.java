package com.example.demo.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleEmailService implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(ConsoleEmailService.class);

    @Override
    public void send(String to, String subject, String bodyHtml) {
        log.info("\n=== EMAIL (simulado) ===\nTO: {}\nSUBJECT: {}\nBODY:\n{}\n========================", to, subject, bodyHtml);
    }
}
