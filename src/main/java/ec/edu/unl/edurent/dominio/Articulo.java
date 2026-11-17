package ec.edu.unl.edurent.dominio;

public abstract class Articulo {
    public static final double PorcentajeGrantia = 0.30;
    public static final String DISPONIBLE = "DISPONIBLE";
    public static final String ALQUILADO = "ALQUILADO";
    public static final String NO_DISPONIBLE = "NO DISPONIBLE";

    private static int contadorArticulos = 0;

    private String idArticulo;
    private String titulo;
    private String descripcion;
    private double precioEstimado;
    private String estadoDisponibilidad;

    protected Articulo(String titulo, String descripcion, double precioEstimado) {
        setTitulo(titulo);
        setDescripcion(descripcion);
        setPrecioEstimado(precioEstimado);
        this.estadoDisponibilidad = DISPONIBLE;
    }

    protected final void registrarCreacion() {
        contadorArticulos++;
        this.idArticulo = String.format("ART-%03d", contadorArticulos);
    }

    public static int getContadorArticulos() {
        return contadorArticulos;
    }

    public String getIdArticulo() { return idArticulo; }
    public String getTitulo() { return titulo; }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo del articulo no puede estar vacio.");
        }
        this.titulo = titulo.trim();
    }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) {
        if (descripcion == null) {
            throw new IllegalArgumentException("La descripcion no puede ser nula.");
        }
        this.descripcion = descripcion.trim();
    }

    public double getPrecioEstimado() { return precioEstimado; }

    public void setPrecioEstimado(double precioEstimado) {
        if (precioEstimado <= 0) {
            throw new IllegalArgumentException("El precio estimado debe ser mayor que cero. Valor recibido: " + precioEstimado);
        }
        this.precioEstimado = precioEstimado;
    }

    public String getEstadoDisponibilidad() { return estadoDisponibilidad; }

    public boolean isDisponible() {
        return DISPONIBLE.equals(estadoDisponibilidad);
    }

    public void actualizarEstado(String nuevoEstado) {
        if (!DISPONIBLE.equals(nuevoEstado) && !ALQUILADO.equals(nuevoEstado) && !NO_DISPONIBLE.equals(nuevoEstado)) {
            throw new IllegalArgumentException("Estado de disponibilidad no valido: " + nuevoEstado);
        }
        this.estadoDisponibilidad = nuevoEstado;
    }

    public double calcularValorGarantia() {
        return precioEstimado * PorcentajeGrantia;
    }

    public String mostrarDetalles() {
        return String.format("[%s] %s | precio estimado: $%.2f | estado: %s",
                idArticulo, titulo, precioEstimado, estadoDisponibilidad);
    }
}