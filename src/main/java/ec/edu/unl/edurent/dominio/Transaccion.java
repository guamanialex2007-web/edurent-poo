package ec.edu.unl.edurent.dominio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaccion {
    public static final String TIPO_ALQUILER = "ALQUILER";
    public static final String TIPO_VENTA = "VENTA";
    public static final String PENDIENTE = "PENDIENTE";
    public static final String EN_CURSO = "EN_CURSO";
    public static final String ENTREGADA = "ENTREGADA";
    public static final String FINALIZADA = "FINALIZADA";
    public static final String CANCELADA = "CANCELADA";
    public static final double TARIFA_DIARIA = 0.02;

    private static int contadorTransacciones = 0;

    private final String idTransaccion;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final String tipoTransaccion;
    private String estadoActual;
    private final double montoTotal;
    private final Estudiante arrendatario;
    private final Articulo articulo;
    private final PuntoEntrega puntoEntrega;
    private DepositoGarantia deposito;

    public Transaccion(Estudiante arrendatario, Articulo articulo, PuntoEntrega puntoEntrega,
                       LocalDate fechaInicio, LocalDate fechaFin, String tipoTransaccion) {
        if (arrendatario == null || articulo == null || puntoEntrega == null) {
            throw new IllegalArgumentException("Estudiante, articulo y punto de entrega son obligatorios.");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas son obligatorias.");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin (" + fechaFin
                    + ") no puede ser anterior a la de inicio (" + fechaInicio + ").");
        }
        if (!TIPO_ALQUILER.equals(tipoTransaccion) && !TIPO_VENTA.equals(tipoTransaccion)) {
            throw new IllegalArgumentException("Tipo de transaccion no valido: " + tipoTransaccion);
        }

        this.arrendatario = arrendatario;
        this.articulo = articulo;
        this.puntoEntrega = puntoEntrega;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoTransaccion = tipoTransaccion;

        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        this.montoTotal = TIPO_ALQUILER.equals(tipoTransaccion)
                ? articulo.getPrecioEstimado() * TARIFA_DIARIA * dias
                : articulo.getPrecioEstimado();

        this.estadoActual = PENDIENTE;
        contadorTransacciones++;
        this.idTransaccion = String.format("TRX-%03d", contadorTransacciones);
    }

    public String getIdTransaccion() { return idTransaccion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public String getTipoTransaccion() { return tipoTransaccion; }
    public String getEstadoActual() { return estadoActual; }
    public double getMontoTotal() { return montoTotal; }
    public Estudiante getArrendatario() { return arrendatario; }
    public Articulo getArticulo() { return articulo; }
    public PuntoEntrega getPuntoEntrega() { return puntoEntrega; }
    public DepositoGarantia getDeposito() { return deposito; }

    public void iniciarTransaccion() {
        if (!PENDIENTE.equals(estadoActual)) {
            throw new IllegalStateException("Solo se puede iniciar una transaccion PENDIENTE. Estado: " + estadoActual);
        }
        if (!articulo.isDisponible()) {
            throw new IllegalStateException("El articulo " + articulo.getIdArticulo()
                    + " no esta disponible (estado: " + articulo.getEstadoDisponibilidad() + ").");
        }
        articulo.actualizarEstado(Articulo.ALQUILADO);
        if (TIPO_ALQUILER.equals(tipoTransaccion)) {
            deposito = new DepositoGarantia(articulo.calcularValorGarantia());
            deposito.procesarRetencion();
        }
        estadoActual = EN_CURSO;
    }

    public void confirmarEntrega() {
        if (!EN_CURSO.equals(estadoActual)) {
            throw new IllegalStateException("Solo se puede confirmar la entrega de una transaccion EN_CURSO.");
        }
        puntoEntrega.registrarEncuentro(this);
        estadoActual = ENTREGADA;
    }

    public void finalizarTransaccion() {
        if (!ENTREGADA.equals(estadoActual)) {
            throw new IllegalStateException("Solo se puede finalizar una transaccion ENTREGADA.");
        }
        if (TIPO_ALQUILER.equals(tipoTransaccion)) {
            articulo.actualizarEstado(Articulo.DISPONIBLE);
            deposito.liberarFondos();
        } else {
            articulo.actualizarEstado(Articulo.NO_DISPONIBLE);
        }
        estadoActual = FINALIZADA;
    }

    public void cancelarTransaccion() {
        if (!PENDIENTE.equals(estadoActual) && !EN_CURSO.equals(estadoActual)) {
            throw new IllegalStateException("No se puede cancelar una transaccion en estado " + estadoActual);
        }
        if (EN_CURSO.equals(estadoActual)) {
            articulo.actualizarEstado(Articulo.DISPONIBLE);
            if (deposito != null) {
                deposito.liberarFondos();
            }
        }
        estadoActual = CANCELADA;
    }
}