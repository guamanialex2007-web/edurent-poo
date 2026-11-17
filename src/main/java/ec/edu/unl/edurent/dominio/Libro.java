package ec.edu.unl.edurent.dominio;

public class Libro extends Articulo {
    public static final double PorcentjeGarantiaLiibrO = 0.20;

    private String autor;
    private String edicion;
    private String isbn;

    public Libro(String titulo, String descripcion, double precioEstimado,
                 String autor, String edicion, String isbn) {
        super(titulo, descripcion, precioEstimado);
        setAutor(autor);
        setEdicion(edicion);
        setIsbn(isbn);
        registrarCreacion();
    }

    public String getAutor() { return autor; }

    public void setAutor(String autor) {
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("El autor no puede estar vacio.");
        }
        this.autor = autor.trim();
    }

    public String getEdicion() { return edicion; }

    public void setEdicion(String edicion) {
        if (edicion == null || edicion.isBlank()) {
            throw new IllegalArgumentException("La edicion no puede estar vacia.");
        }
        this.edicion = edicion.trim();
    }

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) {
        if (isbn == null) {
            throw new IllegalArgumentException("El ISBN no puede ser nulo.");
        }
        String limpio = isbn.replace("-", "").trim();
        if (!limpio.matches("\\d{10}|\\d{13}")) {
            throw new IllegalArgumentException("ISBN invalido (debe tener 10 o 13 digitos): " + isbn);
        }
        this.isbn = limpio;
    }

    @Override
    public double calcularValorGarantia() {
        return getPrecioEstimado() * PorcentjeGarantiaLiibrO;
    }

    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles() + " | libro de " + autor + ", ed. " + edicion;
    }
}