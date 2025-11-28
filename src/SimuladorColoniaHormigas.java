import java.util.HashMap;
import java.util.Random;

public class SimuladorColoniaHormigas {
    private static final int NUMERO_OBRERAS = 1;
    private static final int NUMERO_TRABAJADORAS = 1;
    private static final int NUMERO_GUARDIANAS = 2;
    private static final int NUMERO_NOSTALGICAS = 2;
    private static final int INTERVALO_ACTUALIZACION = 1500;
    private static final int[][] DIRECCIONES = {
            {1,0},  //Abajo
            {-1,0}, //Arriba
            {0,1},  //Derecha
            {0,-1}  //Izquierda
    };
    private Mapa mapa;
    private HashMap<String, Hormiga> hormigas;
    private volatile boolean simulacionActiva;
    private final Random random;

    /*
    Asigno los valores iniciales en el propio constructor.
     */
    public SimuladorColoniaHormigas() {
        this.random = new Random();
        this.mapa = new Mapa();
        this.hormigas = new HashMap<>();
        this.simulacionActiva = false;
    }

    /**
     * Creo tantas hormigas obreras como me indique el atributo NUMERO_OBRERAS
     * Big O(?) no estoy seguro de la complejidad, ya que depende de números aleatorios.
     */
    public void generarHormigaObrera() {
        Posicion posicionHormiguero = mapa.getHormiguero();

        int hormigasObreras_agregadas = 0;
        while (hormigasObreras_agregadas < NUMERO_OBRERAS) {
            Posicion intentoHormiga = new Posicion(this.random.nextInt(Mapa.ANCHO),this.random.nextInt(Mapa.ALTO));
            if (this.mapa.dentroLimites(intentoHormiga)) { //Distinta posición al hormiguero
                hormigasObreras_agregadas += 1; //El intento es exitoso así que aumento las hormigas agregadas.
                /*
                Creo una nueva HormigaObrera con un id y una Posicion (la posicion es la generada anteriormente)
                 */
                String idHormiga = "Hormiga_O_" + hormigasObreras_agregadas;
                HormigaObrera obrera = new HormigaObrera(idHormiga, intentoHormiga, this);
                this.hormigas.put(idHormiga,obrera);
                obrera.start(); //Se inicia el hilo en la clase Hormiga
            }
        }

        int hormigasTrabajadoras_agregadas = 0;
        while (hormigasTrabajadoras_agregadas < NUMERO_TRABAJADORAS) {
            Posicion intentoHormiga = new Posicion(this.random.nextInt(Mapa.ANCHO),this.random.nextInt(Mapa.ALTO));
            if (this.mapa.dentroLimites(intentoHormiga)) {
                hormigasTrabajadoras_agregadas += 1;
                String idHormiga = "Hormiga_T_" + hormigasTrabajadoras_agregadas;
                HormigaTrabajadora trabajadora = new HormigaTrabajadora(idHormiga, intentoHormiga, this);
                this.hormigas.put(idHormiga,trabajadora);
                trabajadora.start();
            }
        }

        int hormigasGuardianas_agregadas = 0;
        while (hormigasGuardianas_agregadas < NUMERO_GUARDIANAS) {
            Posicion intentoHormiga = new Posicion(this.random.nextInt(Mapa.ANCHO),this.random.nextInt(Mapa.ALTO));
            if (this.mapa.dentroLimites(intentoHormiga)) {
                hormigasGuardianas_agregadas += 1;
                String idHormiga = "Hormiga_G_" + hormigasGuardianas_agregadas;
                HormigaGuardiana guardiana = new HormigaGuardiana(idHormiga, intentoHormiga, this);
                this.hormigas.put(idHormiga,guardiana);
                guardiana.start();
            }
        }

        int hormigasNostalgicas_agregadas = 0;
        while (hormigasNostalgicas_agregadas < NUMERO_NOSTALGICAS) {
            Posicion intentoHormiga = new Posicion(this.random.nextInt(Mapa.ANCHO),this.random.nextInt(Mapa.ALTO));
            if (this.mapa.dentroLimitesNostalgica(intentoHormiga)) {
                hormigasNostalgicas_agregadas += 1;
                String idHormiga = "Hormiga_N_" + hormigasNostalgicas_agregadas;
                HormigaNostalgica nostalgica = new HormigaNostalgica(idHormiga, intentoHormiga, this);
                this.hormigas.put(idHormiga,nostalgica);
                nostalgica.start();
            }
        }
    }

    /**
     * Ejecuta la simulación, dejando un intervalo entre ejecuciones/iteraciones. Control de errores en Thread.sleep
     * Big O(infinito) no se detiene, es un bucle infinito
     */
    public void ejecutar() {
        generarHormigaObrera();
        this.simulacionActiva = true;
        while (this.simulacionActiva) {
            actualizarVisualizacion();
            try {
                Thread.sleep(INTERVALO_ACTUALIZACION);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Detiene la simulación y los hilos de las hormigas.
     * Big O(n) n=cantidad de hormigas
     */
    public void detenerSimulacion() {
        this.simulacionActiva = false;
        for (Hormiga hormiga : hormigas.values()) {
            hormiga.detener();
        }
    }

    /**
     * Imprime el estado actual del mapa, actualizando primero el propio mapa.
     * Big O(n) n=cantidad de hormigas
     */
    public void actualizarVisualizacion() {
        limpiarConsola();
        this.mapa.prepararMapa(this.hormigas); //repararMapa() recibe String->id_hormiga y Hormiga->objHormigaObrera
        this.mapa.mostrarMapa();
        mostrarEstadisticas();
    }

    /**
     * Método que cambia los valores de Posicion de una instancia de la clase hormiga.
     * Big O(?) depende de números aleatorios, no para hasta generar una posición válida.
     * @param hormiga Instancia de la clase hormiga
     */
    public synchronized void moverHormigaAleatoriamente(Hormiga hormiga) {
        boolean movimiento_posible = false;

        while (!movimiento_posible) {
            int direccion = this.random.nextInt(4); //Genera un número entre 0-3
            int deltaY = DIRECCIONES[direccion][0]; // En DIRECCIONES[][] el elemento[n][0] corresponde a movimiento en eje X
            int deltaX = DIRECCIONES[direccion][1]; // En DIRECCIONES[][] el elemento[n][1] corresponde a movimiento en eje Y
            Posicion posicion = hormiga.getPosicion(); // Posición actual de la hormiga.
            Posicion nuevaPosicion = posicion.mover(deltaX, deltaY); // Pasamos las deltas a la función mover para generar nueva posición.

            /*
            Generar movimiento para la clase HormigaObrera.
            Se mueve una casilla a la vez y no puede estar en la diagonal principal del mapa.
             */
            if (hormiga.getTipo() == TipoHormiga.OBRERA) {
                if (this.mapa.dentroLimites(nuevaPosicion) && (this.mapa.getHormiguero().getX() != nuevaPosicion.getX() || this.mapa.getHormiguero().getY() != nuevaPosicion.getY())) {
                    if (nuevaPosicion.getX() != nuevaPosicion.getY()) {
                        hormiga.setPosicion(nuevaPosicion);
                        movimiento_posible = true;
                    }
                }
            }
            /*
            Generar movimiento para la clase HormigaTrabajadora.
            Se mueve una casilla a la vez y no tiene restricciones de movimiento.
             */
            if (hormiga.getTipo() == TipoHormiga.TRABAJADORA) {
                if (this.mapa.dentroLimites(nuevaPosicion) && (this.mapa.getHormiguero().getX() != nuevaPosicion.getX() || this.mapa.getHormiguero().getY() != nuevaPosicion.getY())) {
                    hormiga.setPosicion(nuevaPosicion);
                    movimiento_posible = true;
                }
            }
            /*
            Generar movimiento para la clase HormigaGuardiana.
            Se mueve una casilla a la vez y solo se mueve en el eje Y.
            */
            if (hormiga.getTipo() == TipoHormiga.GUARDIANA) {
                if (this.mapa.dentroLimites(nuevaPosicion) && (this.mapa.getHormiguero().getX() != nuevaPosicion.getX() || this.mapa.getHormiguero().getY() != nuevaPosicion.getY())) {
                    if (nuevaPosicion.getX() == hormiga.getPosicion().getX()) {
                        hormiga.setPosicion(nuevaPosicion);
                        movimiento_posible = true;
                    }
                }
            }
            /*
            Generar movimiento para la clase HormigaNostalgica.
            Se mueve una casilla a la vez y no se puede alejar más de dos casillas del hormiguero.
            */
            if (hormiga.getTipo() == TipoHormiga.NOSTALGICA) {
                if (this.mapa.dentroLimites(nuevaPosicion) && (this.mapa.getHormiguero().getX() != nuevaPosicion.getX() || this.mapa.getHormiguero().getY() != nuevaPosicion.getY())) {
                    if (this.mapa.dentroLimitesNostalgica(nuevaPosicion)) {
                        hormiga.setPosicion(nuevaPosicion);
                        movimiento_posible = true;
                    }
                }
            }
        }
    }

    /**
     * Este método imprime 10 saltos e linea, ayudando a leer fácilmente la terminal.
     * Big O(n) n=cantidad de lineas en blanco
     */
    public void limpiarConsola() {
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
    }

    /**
     * Método que imprime las estadisticas de la colonia de hormigas.
     * Big O(n) n=cantidad de hormigas
     */
    public void mostrarEstadisticas() {
        int hormigas_obreras = 0;
        int hormigas_trabajadoras = 0;
        int hormigas_guardianas = 0;
        int hormigas_nostalgicas = 0;

        for (Hormiga hormiga : hormigas.values()) {
            switch (hormiga.getTipo()) {
                case OBRERA -> hormigas_obreras++;
                case TRABAJADORA -> hormigas_trabajadoras++;
                case GUARDIANA -> hormigas_guardianas++;
                case NOSTALGICA -> hormigas_nostalgicas++;
            }
        }

        System.out.println("======================================");
        System.out.println("Estadísticas de la simulación: ");
        System.out.println("======================================");
        System.out.println("Hormigas activas: " + hormigas.size());
        System.out.println("Hormigas obreras: " + hormigas_obreras);
        System.out.println("Hormigas trabajadoras: " + hormigas_trabajadoras);
        System.out.println("Hormigas guardianas: " + hormigas_guardianas);
        System.out.println("Hormigas nostálgicas: " + hormigas_nostalgicas);
        System.out.println("Posición hormiguero: " + mapa.getHormiguero().getX() + " " + mapa.getHormiguero().getY() + ".");
        System.out.println("Intervalo entre ejecuciones: " + INTERVALO_ACTUALIZACION + "ms.");
    }
}