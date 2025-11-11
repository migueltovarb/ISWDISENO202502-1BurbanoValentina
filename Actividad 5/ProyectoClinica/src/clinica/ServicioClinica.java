package clinica;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServicioClinica {

    // “repositorio” en memoria para el ejemplo
    private final List<Propietario> propietarios = new ArrayList<>();
    private final List<Mascota> mascotas = new ArrayList<>();
    private final List<Control> controles = new ArrayList<>();

    public Propietario registrarPropietario(String nombreCompleto, String identificacion, String contacto) {
        Propietario p = new Propietario(nombreCompleto, identificacion, contacto);
        propietarios.add(p);
        return p;
    }

    public Mascota registrarMascota(String nombre, String especie, int edad, Propietario duenio) {
        Mascota m = new Mascota(nombre, especie, edad);
        m.setDuenio(duenio);
        mascotas.add(m);
        return m;
    }

    public Control registrarControl(Control control, Mascota mascota) {
        if (control == null || mascota == null) return null;
        mascota.agregarControl(control);
        controles.add(control);
        return control;
    }

    /** Devuelve un string con todas las mascotas y sus controles */
    public String obtenerHistorialControles() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter f = DateTimeFormatter.ISO_LOCAL_DATE;

        for (Mascota m : mascotas) {
            sb.append("Mascota: ").append(m.getNombre())
              .append(" (").append(m.getEspecie()).append(")")
              .append(" - Dueño: ").append(m.getDuenio() != null ? m.getDuenio().getNombreCompleto() : "—")
              .append("\n");

            if (m.getResumenControles().isEmpty()) {
                sb.append("  * Sin controles registrados\n");
            } else {
                for (Control c : m.getResumenControles()) {
                    sb.append("  * ")
                      .append(c.getTipo()).append(" | ")
                      .append(c.getFecha() != null ? f.format(c.getFecha()) : "s/fecha")
                      .append(" | ").append(c.getObservaciones() != null ? c.getObservaciones() : "")
                      .append("\n");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /** Resumen simple por mascota */
    public String resumenMascota(Mascota m) {
        if (m == null) return "";
        return m.toString();
    }

    // Getters (por si quieres inspeccionar “repositorios” en tests)
    public List<Propietario> getPropietarios() { return propietarios; }
    public List<Mascota> getMascotas() { return mascotas; }
    public List<Control> getControles() { return controles; }
}
