package ec.edu.unl.edurent.dominio;

/**
 * Parte de una Transaccion (composicion): solo Transaccion puede crearlo.
 */
public class DepositoGarantia {
    public static final String PENDIENTE = "PENDIENTE";
    public static final String RETENIDO = "RETENIDO";
    public static final String LIBERADO = "LIBERADO";
    public static final String PENALIZADO = "PENALIZADO";

    private static int contadorGarantias = 0;

    private final String idGarantia;
    private double montoRetenido;
    private String estadoGarantia;
    private String motivoRetencion;

    DepositoGarantia(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de la garantia debe ser mayor que cero.");
        }
        contadorGarantias++;
        this.idGarantia = String.format("GAR-%03d", contadorGarantias);
        this.montoRetenido = monto;
        this.estadoGarantia = PENDIENTE;
        this.motivoRetencion = "Sin retencion";
    }

    public String getIdGarantia() {
        return idGarantia;
    }

    public double getMontoRetenido() {
        return montoRetenido;
    }

    public String getEstadoGarantia() {
        return estadoGarantia;
    }

    public String getMotivoRetencion() {
        return motivoRetencion;
    }

    public boolean procesarRetencion() {
        if (montoRetenido <= 0 || !PENDIENTE.equals(estadoGarantia)) {
            return false;
        }
        estadoGarantia = RETENIDO;
        motivoRetencion = "Garantia por alquiler de articulo";
        return true;
    }

    public void liberarFondos() {
        if (!RETENIDO.equals(estadoGarantia) && !PENALIZADO.equals(estadoGarantia)) {
            throw new IllegalStateException("No hay fondos retenidos para liberar. Estado: " + estadoGarantia);
        }
        estadoGarantia = LIBERADO;
    }

    public void ejecutarPenalizacion(double penalizacion) {
        if (penalizacion <= 0 || penalizacion > montoRetenido) {
            throw new IllegalArgumentException(
                    "La penalizacion debe estar entre 0 y el monto retenido (" + montoRetenido + ").");
        }
        montoRetenido = penalizacion;
        estadoGarantia = PENALIZADO;
        motivoRetencion = "Penalizacion aplicada: $" + String.format("%.2f", penalizacion);
    }
}