package clinica;

import java.util.ArrayList;
import java.util.List;

public class Mascota {
    private String nombre;
    private String especie;
    private int    edad;

    // Relaciones
    private Propietario duenio;                 // 1 propietario
    private List<Control> resumenControles = new ArrayList<>(); // 1..* controles

    public Mascota() {}

    public Mascota(String nombre, String especie, int edad) {
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Propietario getDuenio() {
        return duenio;
    }

    public void setDuenio(Propietario duenio) {
        this.duenio = duenio;
        if (duenio != null && !duenio.getMascotas().contains(this)) {
            duenio.agregarMascota(this); // mantiene la relación coherente
        }
    }

    public List<Control> getResumenControles() {
        return resumenControles;
    }

    public void setResumenControles(List<Control> resumenControles) {
        this.resumenControles = (resumenControles != null) ? resumenControles : new ArrayList<>();
    }

    public void agregarControl(Control c) {
        if (c != null) resumenControles.add(c);
    }

    @Override
    public String toString() {
        return "Mascota{" +
                "nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", edad=" + edad +
                ", duenio=" + (duenio != null ? duenio.getNombreCompleto() : "—") +
                ", controles=" + resumenControles.size() +
                '}';
    }
}
