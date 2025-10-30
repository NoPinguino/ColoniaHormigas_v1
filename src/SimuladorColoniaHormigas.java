import java.util.HashMap;
import java.util.Random;

public class SimuladorColoniaHormigas {
    private static final int NUMERO_OBRERAS = 3;
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
    public SimuladorColoniaHormigas(Random random, Mapa mapa, HashMap<String, Hormiga> hormigas, boolean simulacionActiva) {
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
        int hormigas_agregadas = 0;
        Posicion posicionHormiguero = mapa.getHormiguero();
        while (hormigas_agregadas < NUMERO_OBRERAS) {
            Posicion intentoHormiga = new Posicion(this.random.nextInt(Mapa.ANCHO),this.random.nextInt(Mapa.ALTO));
            if (intentoHormiga.getX() != posicionHormiguero.getX() && intentoHormiga.getY() != posicionHormiguero.getY()) {
                hormigas_agregadas += 1; //El intento es exitoso así que aumento las hormigas agregadas.
                /*
                Creo una nueva HormigaObrera con un id y una Posicion (la posicion es la generada anteriormente)
                 */
                String idHormiga = "Hormiga_" + hormigas_agregadas;
                HormigaObrera obrera = new HormigaObrera(idHormiga, intentoHormiga);
                this.hormigas.put(idHormiga,obrera);
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
    private void actualizarVisualizacion() {
        limpiarConsola();
        this.mapa.prepararMapa(this.hormigas); //repararMapa() recibe String->id_hormiga y Hormiga->objHormigaObrera
        this.mapa.mostrarMapa();
    }

    /**
     * Método que recorre el mapa hormigas, pasando cada hormiga indivialmente a moverHormigaAleatoriamente()
     * Big O(n)
     */
    private synchronized void moverTodasLasHormigas() {
        for (Hormiga hormiga : this.hormigas.values()) {
            moverHormigaAleatoriamente(hormiga);
        }
    }

    /**
     * Método que cambia los valores de Posicion de una instancia de la clase hormiga.
     * Big O(?) depende de números aleatorios, no para hasta generar una posición válida.
     * @param hormiga Instancia de la clase hormiga
     */
    private synchronized void moverHormigaAleatoriamente(Hormiga hormiga) {
        boolean movimiento_posible = false;
        while (!movimiento_posible) {
            int direccion = this.random.nextInt(4); //Genera un número entre 0-3
            int deltaX = DIRECCIONES[direccion][0]; // En DIRECCIONES[][] el elemento[n][0] corresponde a movimiento en eje Y
            int deltaY = DIRECCIONES[direccion][1]; // En DIRECCIONES[][] el elemento[n][1] corresponde a movimiento en eje X

            Posicion posicion = hormiga.getPosicion();
            Posicion nuevaPosicion = posicion.mover(deltaX, deltaY);

            /*
            Comprueba que las coordenadas se encuentren en los límites del tablero llamando a dentroLimites()
            Compara de forma individual los valores X y los valores Y de hormiguero y nueva posición, si al
            menos uno de esos dos valores es distinto al otro, las posiciones de hormiguero y hormiga son distintas.
            Si ambas comparaciones devuelven true, la hormiga se puede mover, si no, lo vuelve a intentar en la
            próxima iteración de while().
             */
            if (this.mapa.dentroLimites(nuevaPosicion) && (this.mapa.getHormiguero().getX() != nuevaPosicion.getX() || this.mapa.getHormiguero().getY() != nuevaPosicion.getY())) {
                hormiga.setPosicion(nuevaPosicion);
                movimiento_posible = true;
            }
        }

    }

    /**
     * Este método imprime 10 saltos e linea, ayudando a leer fácilmente la terminal.
     * Big O(n) n=cantidad de lineas en blanco
     */
    private void limpiarConsola() {
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
    }

    /**
     * Método que imprime las estadisticas de la colonia de hormigas.
     */
    private void mostrarEstadisticas() {
        int hormigas_activas = 0;
        for (Hormiga hormiga : hormigas.values()) {
            if (hormiga.isActiva()) hormigas_activas++;
        }
        int hormigas_obreras = 0;
        for (Hormiga hormiga : hormigas.values()) {
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