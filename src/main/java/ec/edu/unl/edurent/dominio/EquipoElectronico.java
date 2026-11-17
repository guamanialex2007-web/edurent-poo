package ec.edu.unl.edurent.dominio;

public class EquipoElectronico extends Articulo {
    public static final int VidaUtilMese = 60;
    public static final double PorcentajeGarantiaEquipo = 0.40;

    private String marca;
    private String modelo;
    private int tiempoUsoMeses;

    public EquipoElectronico(String titulo, String descripcion, double precioEstimado,
                             String marca, String modelo, int tiempoUsoMeses) {
        super(titulo, descripcion, precioEstimado);
        setMarca(marca);
        setModelo(modelo);
        setTiempoUsoMeses(tiempoUsoMeses);
        registrarCreacion();
    }

    public String getMarca() { return marca; }

    public void setMarca(String marca) {
        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("La marca no puede estar vacia.");
        }
        this.marca = marca.trim();
    }

    public String getModelo() { return modelo; }

    public void setModelo(String modelo) {
        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("El modelo no puede estar vacio.");
        }
        this.modelo = modelo.trim();
    }

    public int getTiempoUsoMeses() { return tiempoUsoMeses; }

    public void setTiempoUsoMeses(int tiempoUsoMeses) {
        if (tiempoUsoMeses < 0) {
            throw new IllegalArgumentException("El tiempo de uso no puede ser negativo.");
        }
        this.tiempoUsoMeses = tiempoUsoMeses;
    }

    public boolean verificarFuncionamlento() {
        return tiempoUsoMeses < VidaUtilMese;
    }

    @Override
    public double calcularValorGarantia() {
        return getPrecioEstimado() * PorcentajeGarantiaEquipo;
    }

    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles() + " | equipo: " + marca + " " + modelo
                + " (" + tiempoUsoMeses + " meses de uso)";
    }
}