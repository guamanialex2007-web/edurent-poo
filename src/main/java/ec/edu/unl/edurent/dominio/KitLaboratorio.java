package ec.edu.unl.edurent.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitLaboratorio extends Articulo {
    public static final double PorcentaajeGarantiaKit = 0.35;

    private final List<String> listaComponentes;
    private String nivelDesgaste;
    private int componentesEsperados;

    public KitLaboratorio(String titulo, String descripcion, double precioEstimado,
                          int componentesEsperados, String nivelDesgaste) {
        super(titulo, descripcion, precioEstimado);
        if (componentesEsperados <= 0) {
            throw new IllegalArgumentException("El kit debe tener al menos un componente esperado.");
        }
        this.componentesEsperados = componentesEsperados;
        this.listaComponentes = new ArrayList<>();
        setNivelDesgaste(nivelDesgaste);
        registrarCreacion();
    }

    public List<String> getListaComponentes() {
        return Collections.unmodifiableList(listaComponentes);
    }

    public void agregarComponente(String componente) {
        if (componente == null || componente.isBlank()) {
            throw new IllegalArgumentException("El nombre del componente no puede estar vacio.");
        }
        listaComponentes.add(componente.trim());
    }

    public String getNivelDesgaste() { return nivelDesgaste; }

    public void setNivelDesgaste(String nivelDesgaste) {
        if (!"BAJO".equals(nivelDesgaste) && !"MEDIO".equals(nivelDesgaste) && !"ALTO".equals(nivelDesgaste)) {
            throw new IllegalArgumentException("Nivel de desgaste invalido (use BAJO, MEDIO o ALTO): " + nivelDesgaste);
        }
        this.nivelDesgaste = nivelDesgaste;
    }

    public boolean verificarComponentesCompletos() {
        return listaComponentes.size() == componentesEsperados;
    }

    @Override
    public double calcularValorGarantia() {
        return getPrecioEstimado() * PorcentaajeGarantiaKit;
    }

    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles() + " | kit: " + listaComponentes.size() + "/"
                + componentesEsperados + " componentes, desgaste " + nivelDesgaste;
    }
}