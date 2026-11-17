package ec.edu.unl.edurent.vista;

import ec.edu.unl.edurent.dominio.*;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static void prueba(String nombre, Runnable accion) {
        System.out.println("\n--- " + nombre + " ---");
        try {
            accion.run();
        } catch (IllegalArgumentException | IllegalStateException | InputMismatchException e) {
            System.out.println("[EXCEPCION CONTROLADA] " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Estudiante propietario = new Estudiante("Ana", "Lopez", "ana.lopez@unl.edu.ec", "Universidad Nacional de Loja");
        Estudiante arrendatario = new Estudiante("Luis", "Mora", "luis.mora@utpl.edu.ec", "UTPL");

        Libro libro = new Libro("Java 17: Fundamentos", "Libro de POO", 40.0, "J. M. Vegas", "1ra", "978-84-9964-001-2");
        EquipoElectronico calc = new EquipoElectronico("Calculadora cientifica", "Casio fx-991", 25.0, "Casio", "fx-991LA", 18);
        KitLaboratorio kit = new KitLaboratorio("Kit de electronica", "Protoboard y sensores", 60.0, 3, "BAJO");

        PuntoEntrega punto = new PuntoEntrega("Campus La Argelia", "Biblioteca planta baja", "08:00-18:00");

        prueba("P1 (RF01) Registro de estudiantes validos", () -> {
            propietario.registrarParfi();
            arrendatario.registrarParfi();
            System.out.println("[OK] " + propietario + " | registrado=" + propietario.isRegistrado());
            System.out.println("[OK] " + arrendatario + " | registrado=" + arrendatario.isRegistrado());
            System.out.println("Estudiante.getContadorEstudiantes() = " + Estudiante.getContadorEstudiantes());
        });

        prueba("P2 (RF02) Publicar tres articulos (relacion 1 a 0..*)", () -> {
            kit.agregarComponente("Protoboard");
            kit.agregarComponente("Sensor ultrasonico");
            kit.agregarComponente("Cables");
            propietario.publicarArticulo(libro);
            propietario.publicarArticulo(calc);
            propietario.publicarArticulo(kit);
            System.out.println("Tamano de articulosPublicados = " + propietario.getArticulosPublicados().size());
            System.out.println("Articulo.getContadorArticulos() = " + Articulo.getContadorArticulos());
            System.out.println("Kit completo? " + kit.verificarComponentesCompletos());
        });

        prueba("P3 Precio negativo", () -> new Libro("Algebra", "Texto", -10.0, "Baldor", "2da", "9786074386233"));
        prueba("P4 Titulo vacio", () -> new EquipoElectronico(" ", "desc", 20.0, "HP", "120", 5));
        prueba("P5 Correo no institucional", () -> new Estudiante("Pedro", "Diaz", "pedro@gmail.com", "UNL"));
        prueba("P6 ISBN invalido", () -> new Libro("Calculo", "Texto", 30.0, "Stewart", "7ma", "123"));
        prueba("P7 Publicar sin registrar perfil", () ->
                new Estudiante("Eva", "Rios", "eva.rios@unl.edu.ec", "UNL").publicarArticulo(libro));
        prueba("P8 Calificacion fuera de rango", () -> arrendatario.calificarUsuario(7));

        prueba("P9 Reputacion promedio (calificar 5 y 4)", () -> {
            propietario.calificarUsuario(5);
            propietario.calificarUsuario(4);
            System.out.println("Reputacion de " + propietario.getNombres() + " = " + propietario.getReputacion());
        });

        prueba("P10 Polimorfismo: garantia y detalles por tipo de articulo", () -> {
            Articulo[] articulos = {libro, calc, kit};
            for (Articulo a : articulos) {
                System.out.println(a.mostrarDetalles());
                System.out.printf("   garantia = $%.2f%n", a.calcularValorGarantia());
            }
        });

        prueba("P11 (RF04, RF05) Alquiler completo: iniciar, entregar, finalizar", () -> {
            Transaccion t = new Transaccion(arrendatario, libro, punto,
                    LocalDate.of(2026, 11, 16), LocalDate.of(2026, 11, 20), Transaccion.TIPO_ALQUILER);
            System.out.printf("%s creada | monto $%.2f | estado %s%n", t.getIdTransaccion(), t.getMontoTotal(), t.getEstadoActual());
            t.iniciarTransaccion();
            System.out.println("Iniciada | articulo=" + libro.getEstadoDisponibilidad()
                    + " | garantia=" + t.getDeposito().getEstadoGarantia() + " $" + t.getDeposito().getMontoRetenido());
            t.confirmarEntrega();
            System.out.println("Entregada | encuentros en punto = " + punto.getEncuentros().size());
            t.finalizarTransaccion();
            System.out.println("Finalizada | articulo=" + libro.getEstadoDisponibilidad()
                    + " | garantia=" + t.getDeposito().getEstadoGarantia());
        });

        prueba("P12 Solicitar dos veces el mismo articulo (riesgo del proyecto)", () -> {
            Transaccion t1 = new Transaccion(arrendatario, calc, punto,
                    LocalDate.of(2026, 11, 17), LocalDate.of(2026, 11, 18), Transaccion.TIPO_ALQUILER);
            Transaccion t2 = new Transaccion(propietario, calc, punto,
                    LocalDate.of(2026, 11, 17), LocalDate.of(2026, 11, 18), Transaccion.TIPO_ALQUILER);
            t1.iniciarTransaccion();
            System.out.println(t1.getIdTransaccion() + " iniciada. Articulo=" + calc.getEstadoDisponibilidad());
            t2.iniciarTransaccion();
        });

        prueba("P13 Fecha de fin anterior a la de inicio", () -> new Transaccion(arrendatario, kit, punto,
                LocalDate.of(2026, 11, 20), LocalDate.of(2026, 11, 10), Transaccion.TIPO_ALQUILER));

        prueba("P14 Penalizacion mayor a la garantia", () -> {
            Transaccion t = new Transaccion(arrendatario, kit, punto,
                    LocalDate.of(2026, 11, 16), LocalDate.of(2026, 11, 17), Transaccion.TIPO_ALQUILER);
            t.iniciarTransaccion();
            t.getDeposito().ejecutarPenalizacion(500.0);
        });

        prueba("P15 Punto de entrega fuera de horario (20:00)", () ->
                System.out.println("Disponible a las 20:00? " + punto.verificarDisponibilidad(LocalTime.of(20, 0))
                        + " | a las 10:00? " + punto.verificarDisponibilidad(LocalTime.of(10, 0))));

        prueba("P16 Lectura de numero invalido con Scanner", () -> {
            Scanner sc = new Scanner(new StringReader("abc"));
            System.out.print("Ingrese el precio: (entrada simulada 'abc') ");
            System.out.println(sc.nextDouble());
        });

        prueba("P17 Conversion de texto con NumberFormatException", () -> {
            double precio = Double.parseDouble("12,5x");
            System.out.println(precio);
        });

        System.out.println("\n=== Contadores finales ===");
        System.out.println("Articulos creados: " + Articulo.getContadorArticulos());
        System.out.println("Estudiantes creados: " + Estudiante.getContadorEstudiantes());
    }
}