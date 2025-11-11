package clinica;

import java.time.LocalDate;

public class Control {
    private LocalDate fecha;
    private String observaciones;
    private TipoControl tipo;

    public Control() {}

    public Control(LocalDate fecha, String observaciones, TipoControl tipo) {
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TipoControl getTipo() {
        return tipo;
    }

    public void setTipo(TipoControl tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Control{" +
                "fecha=" + fecha +
                ", observaciones='" + observaciones + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}
