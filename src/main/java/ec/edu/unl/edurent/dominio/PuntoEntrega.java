package ec.edu.unl.edurent.dominio;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PuntoEntrega {
    private static int contadorPuntos = 0;

    private final String idPunto;
    private String campusUniversitario;
    private String ubicacionEspecifica;
    private String horarioAtencion; // formato HH:mm-HH:mm
    private final List<Transaccion> encuentros;

    public PuntoEntrega(String campusUniversitario, String ubicacionEspecifica, String horarioAtencion) {
        setCampusUniversitario(campusUniversitario);
        setUbicacionEspecifica(ubicacionEspecifica);
        setHorarioAtencion(horarioAtencion);
        this.encuentros = new ArrayList<>();
        contadorPuntos++;
        this.idPunto = String.format("PTO-%03d", contadorPuntos);
    }

    public String getIdPunto() {
        return idPunto;
    }

    public String getCampusUniversitario() {
        return campusUniversitario;
    }

    public void setCampusUniversitario(String campus) {
        if (campus == null || campus.isBlank()) {
            throw new IllegalArgumentException("El campus no puede estar vacio.");
        }
        this.campusUniversitario = campus.trim();
    }

    public String getUbicacionEspecifica() {
        return ubicacionEspecifica;
    }

    public void setUbicacionEspecifica(String ubicacion) {
        if (ubicacion == null || ubicacion.isBlank()) {
            throw new IllegalArgumentException("La ubicacion especifica no puede estar vacia.");
        }
        this.ubicacionEspecifica = ubicacion.trim();
    }

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(String horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo.");
        }
        try {
            String[] partes = horario.split("-");
            if (partes.length != 2) {
                throw new IllegalArgumentException("Formato de horario invalido (use HH:mm-HH:mm): " + horario);
            }
            LocalTime inicio = LocalTime.parse(partes[0].trim());
            LocalTime fin = LocalTime.parse(partes[1].trim());
            if (!fin.isAfter(inicio)) {
                throw new IllegalArgumentException("La hora de cierre debe ser posterior a la de apertura.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Horario con horas invalidas: " + horario, e);
        }
        this.horarioAtencion = horario.trim();
    }

    public List<Transaccion> getEncuentros() {
        return Collections.unmodifiableList(encuentros);
    }

    public boolean verificarDisponibilidad() {
        return verificarDisponibilidad(LocalTime.now());
    }

    public boolean verificarDisponibilidad(LocalTime hora) {
        String[] partes = horarioAtencion.split("-");
        LocalTime inicio = LocalTime.parse(partes[0].trim());
        LocalTime fin = LocalTime.parse(partes[1].trim());
        return !hora.isBefore(inicio) && !hora.isAfter(fin);
    }

    public void registrarEncuentro(Transaccion transaccion) {
        if (transaccion == null) {
            throw new IllegalArgumentException("La transaccion no puede ser nula.");
        }
        encuentros.add(transaccion);
    }
}