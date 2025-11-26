import java.util.HashMap;
import java.util.Random;

public class SimuladorColoniaHormigas {
    private static final int NUMERO_OBRERAS = 3;
    private static final int NUMERO_TRABAJADORAS = 2;
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
            if (intentoHormiga.getX() != posicionHormiguero.getX() && intentoHormiga.getY() != posicionHormiguero.getY()) { //Distinta posición al hormiguero
                hormigasObreras_agregadas += 1; //El intento es exitoso así que aumento las hormigas agregadas.
                /*
                Creo una nueva HormigaObrera con un id y una Posicion (la posicion es la generada anteriormente)
                 */
                String idHormiga = "Hormiga_" + hormigasObreras_agregadas;
                HormigaObrera obrera = new HormigaObrera(idHormiga, intentoHormiga, this);
                this.hormigas.put(idHormiga,obrera);
                obrera.start(); //Se inicia el hilo en la clase Hormiga
            }
        }

        int hormigasTrabajadoras_agregadas = 0;
        while (hormigasTrabajadoras_agregadas < NUMERO_TRABAJADORAS) {
            Posicion intentoHormiga = new Posicion(this.random.nextInt(Mapa.ANCHO),this.random.nextInt(Mapa.ALTO));
            if (intentoHormiga.getX() != posicionHormiguero.getX() && intentoHormiga.getY() != posicionHormiguero.getY()) {
                hormigasTrabajadoras_agregadas += 1;
                String idHormiga = "Hormiga_T_" + hormigasTrabajadoras_agregadas;
                HormigaTrabajadora trabajadora = new HormigaTrabajadora(idHormiga, intentoHormiga, this);
                this.hormigas.put(idHormiga,trabajadora);
                trabajadora.start();
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
        int hormigas_activas = 0;
        int hormigas_obreras = 0;
        for (Hormiga hormiga : hormigas.values()) {
            if (hormiga.isActiva()) hormigas_activas++;
            if (hormiga.getTipo() == TipoHormiga.OBRERA) hormigas_obreras++;
        }

        System.out.println("======================================");
        System.out.println("Estadísticas de la simulación: ");
        System.out.println("======================================");
        System.out.println("Hormigas activas: " + hormigas_activas);
        System.out.println("Hormigas obreras: " + hormigas_obreras);
        System.out.println("Posición hormiguero: " + mapa.getHormiguero().getX() + " " + mapa.getHormiguero().getY() + ".");
        System.out.println("Intervalo entre ejecuciones: " + INTERVALO_ACTUALIZACION + "ms.");
    }
}