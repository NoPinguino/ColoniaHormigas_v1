import java.util.Random;

public abstract class Hormiga extends Thread {
    private static final int INTERVALO_MAX = 3000;
    protected final String id;
    protected final TipoHormiga tipo;
    protected volatile Posicion posicion;
    protected volatile boolean activa;
    protected final Random random;
    protected static final int[][] direcciones = {
            {0,1},  //Derecha
            {1,0},  //Abajo
            {0,-1}, //Izquierda
            {-1,0}  //Arriba
    };
    private final SimuladorColoniaHormigas simuladorColoniaHormigas;

    //Constructor
    public Hormiga(String id, TipoHormiga tipo, Posicion posicion, SimuladorColoniaHormigas simuladorColoniaHormigas) {
        this.id = id;
        this.tipo = tipo;
        this.posicion = posicion;
        this.simuladorColoniaHormigas = simuladorColoniaHormigas;
        this.random = new Random();
        this.activa = true;
    }

    /*
    Getter's
     */
    public String getIdHormiga() {
        return id;
    }
    public TipoHormiga getTipo() {
        return tipo;
    }
    public Posicion getPosicion() {
        return posicion;
    }
    /*
    Setter's
     */
    public void setPosicion(Posicion nuevaPosicion) {
        this.posicion = nuevaPosicion;
    }

    /**
     * Comprueba si la hormiga está activa (devuelve true) o no (devuelve false).
     * Big O(1)
     * @return Valor del booleano activa
     */
     public boolean isActiva() {
        return activa;
     }

    /**
     * Establece el valor de activa a 'false', deteniendo su ejecución.
     * Big O(1)
     */
    public void detener() {
        activa = false;
    }

    /**
     *
     *
     */
    @Override
    public void run() {
       while (this.activa) {
           try {
               this.simuladorColoniaHormigas.moverHormigaAleatoriamente(this);
               Thread.sleep(random.nextInt(INTERVALO_MAX));
           } catch (InterruptedException e) {
               Thread.currentThread().interrupt(); //Paro el hilo si me da error.
               break;
           }
       }
    }
}