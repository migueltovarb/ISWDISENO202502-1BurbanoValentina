package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test básico de humo para asegurar que el entorno de pruebas funciona.
 * Este proyecto usa Javalin, no Spring Boot, por lo que se elimina la anotación @SpringBootTest.
 */
class DemoApplicationTests {

	@Test
	void dummySmokeTest() {
		// Simple verificación de que JUnit 5 está operativo
		Assertions.assertTrue(true, "JUnit 5 ejecuta tests correctamente");
	}
}
