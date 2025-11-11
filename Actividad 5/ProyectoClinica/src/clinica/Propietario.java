package clinica;

import java.util.ArrayList;
import java.util.List;

public class Propietario {
    private String nombreCompleto;
    private String identificacion;
    private String contacto;

    // Relación 1..* con Mascota (un propietario puede tener varias mascotas)
    private final List<Mascota> mascotas = new ArrayList<>();

    public Propietario() {}

    public Propietario(String nombreCompleto, String identificacion, String contacto) {
        this.nombreCompleto = nombreCompleto;
        this.identificacion = identificacion;
        this.contacto = contacto;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    /** Manejo de relación bidireccional */
    public void agregarMascota(Mascota m) {
        if (m == null) return;
        if (!mascotas.contains(m)) {
            mascotas.add(m);
        }
        if (m.getDuenio() != this) {
            m.setDuenio(this);
        }
    }

    @Override
    public String toString() {
        return "Propietario{" +
                "nombreCompleto='" + nombreCompleto + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", contacto='" + contacto + '\'' +
                ", mascotas=" + mascotas.size() +
                '}';
    }
}
