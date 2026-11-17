package ec.edu.unl.edurent.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estudiante {
    public static final float REPUTACION_MAXIMA = 5.0f;
    private static final List<String> DOMINIOS_PERMITIDOS = List.of("@unl.edu.ec", "@utpl.edu.ec");
    private static int contadorEstudiantes = 0;

    private final String idEstudiante;
    private String nombres;
    private String apellidos;
    private String correoInstitucional;
    private String universidad;
    private float reputacion;
    private int totalCalificaciones;
    private boolean registrado;
    private final List<Articulo> articulosPublicados;

    public Estudiante(String nombres, String apellidos, String correoInstitucional, String universidad) {
        setNombres(nombres);
        setApellidos(apellidos);
        setCorreoInstitucional(correoInstitucional);
        setUniversidad(universidad);
        this.reputacion = 0f;
        this.totalCalificaciones = 0;
        this.registrado = false;
        this.articulosPublicados = new ArrayList<>();
        contadorEstudiantes++;
        this.idEstudiante = String.format("EST-%03d", contadorEstudiantes);
    }

    public static int getContadorEstudiantes() { return contadorEstudiantes; }
    public String getIdEstudiante() { return idEstudiante; }
    public String getNombres() { return nombres; }

    public void setNombres(String nombres) {
        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException("Los nombres no pueden estar vacios.");
        }
        this.nombres = nombres.trim();
    }

    public String getApellidos() { return apellidos; }

    public void setApellidos(String apellidos) {
        if (apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException("Los apellidos no pueden estar vacios.");
        }
        this.apellidos = apellidos.trim();
    }

    public String getCorreoInstitucional() { return correoInstitucional; }

    public void setCorreoInstitucional(String correo) {
        if (!esCorreoInstitucional(correo)) {
            throw new IllegalArgumentException("El correo no pertenece a un dominio institucional permitido: " + correo);
        }
        this.correoInstitucional = correo.trim().toLowerCase();
    }

    public String getUniversidad() { return universidad; }

    public void setUniversidad(String universidad) {
        if (universidad == null || universidad.isBlank()) {
            throw new IllegalArgumentException("La universidad no puede estar vacia.");
        }
        this.universidad = universidad.trim();
    }

    public float getReputacion() { return reputacion; }
    public boolean isRegistrado() { return registrado; }

    public List<Articulo> getArticulosPublicados() {
        return Collections.unmodifiableList(articulosPublicados);
    }

    private static boolean esCorreoInstitucional(String correo) {
        if (correo == null) return false;
        String c = correo.trim().toLowerCase();
        for (String dominio : DOMINIOS_PERMITIDOS) {
            if (c.endsWith(dominio) && c.length() > dominio.length()) return true;
        }
        return false;
    }

    public boolean verificarCorreoDominio() {
        return esCorreoInstitucional(correoInstitucional);
    }

    public void registrarParfi() {
        if (registrado) {
            throw new IllegalStateException("El estudiante " + idEstudiante + " ya esta registrado.");
        }
        this.registrado = true;
    }

    public void publicarArticulo(Articulo articulo) {
        if (articulo == null) {
            throw new IllegalArgumentException("No se puede publicar un articulo nulo.");
        }
        if (!registrado) {
            throw new IllegalStateException("Debe registrar su perfil antes de publicar articulos.");
        }
        articulosPublicados.add(articulo);
    }

    public void calificarUsuario(int puntos) {
        if (puntos < 1 || puntos > REPUTACION_MAXIMA) {
            throw new IllegalArgumentException("La calificacion debe estar entre 1 y 5. Valor recibido: " + puntos);
        }
        reputacion = (reputacion * totalCalificaciones + puntos) / (totalCalificaciones + 1);
        totalCalificaciones++;
    }

    @Override
    public String toString() {
        return idEstudiante + " " + nombres + " " + apellidos + " (" + universidad + ")";
    }
}