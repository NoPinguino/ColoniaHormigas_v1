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
     * Big O(NUMERO_OBRERAS * ANCHO * ALTO)
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
     *
     */
    public void ejecutar() {

    }

    /**
     *
     */
    public void detenerSimulacion() {

    }

    /**
     *
     */
    private void actualizarVisualizacion() {

    }

    /**
     *
     */
    private synchronized void moverTodasLasHormigas() {

    }

    /**
     *
     * @param hormiga
     */
    private synchronized void moverHormigaAleatoriamente(Hormiga hormiga) {

    }

    /**
     *
     */
    private void limpiarConsola() {

    }

    /**
     *
     */
    private void mostrarEstadisticas() {

    }
}