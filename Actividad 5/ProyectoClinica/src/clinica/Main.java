package clinica;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ServicioClinica svc = new ServicioClinica();

        Propietario ana = svc.registrarPropietario("Ana Pérez", "ID-123", "555-0101");
        Mascota toby = svc.registrarMascota("Toby", "Canino", 3, ana);

        Control c1 = new Control(LocalDate.of(2025, 1, 10), "Primera vacuna", TipoControl.VACCINE);
        Control c2 = new Control(LocalDate.of(2025, 3, 5), "Desparasitación trimestral", TipoControl.DEWORMING);

        svc.registrarControl(c1, toby);
        svc.registrarControl(c2, toby);

        System.out.println(svc.resumenMascota(toby));
        System.out.println("-----");
        System.out.println(svc.obtenerHistorialControles());
    }
}
