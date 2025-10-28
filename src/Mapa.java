import java.util.HashMap;

public class Mapa {
    public static final int ANCHO = 7;
    public static final int ALTO = 5;
    private final Posicion hormiguero;
    private final char[][] mapa;
    public static final char VACIO = '*';
    public static final char HORMIGUERO = 'H';

    /**
     * Constructor: crea el tablero situando el hormiguero en el centro del mismo.
     * Big O(n*m)
     */
    public Mapa() {
        this.mapa = new char[ALTO][ANCHO];
        /*
        Relleno el mapa con caracteres que representan el espacio vacío, en este caso '*':
         */
        for (int i = 0; i < ANCHO; i++) {
            for (int j = 0; j < ALTO; j++) {
                mapa[i][j] = VACIO;
            }
        }
        /*
        En caso de que el tablero sea par el hormiguero se sitúa en la esquina superior izquierda
        del centro exacto.
         */
        hormiguero = new Posicion(ANCHO/2, ALTO/2); //Guardamos la posición para usarla más adelante.
        this.mapa[hormiguero.getX()][hormiguero.getY()] = HORMIGUERO; //Dibujamos el hormiguero.
    }

    /**
     * Big O(1)
     * @return Devuelve Posicion de hormiguero (x,y)
     */
    public Posicion getHormiguero() {
        return hormiguero;
    }

    /**
     * Comprueba que una posición recibida se encuentre entre los límites de la matriz mapa.
     * Big O(1)
     * @param posicion recibe una posicion con sus coordenadas X e Y
     * @return Devuelve lo que recibe de posicion.dentroLimites(), siendo un boolean true/false.
     */
    public boolean dentroLimites(Posicion posicion) {
        return posicion.dentroLimites(ANCHO,ALTO);
    }

    /**
     * Imprime el mapa con las hormigas y el hormiguero.
     * Big O(n*m)
     */
    public synchronized void mostrarMapa() {
        for (int i = 0; i < ALTO; i++) {
            for (int j = 0; j < ANCHO; j++) {
                System.out.println(this.mapa[i][j] + " ");
            }
            System.out.println(); //Salto de linea
        }
        /*
        Imprimo en consola una pequeña leyenda con los iconos de Hormiguero y HormigaObrera, siendo que son
        los únicos añadidos por el momento en el mapa en esta versión.
         */
        System.out.println("Hormiguero: " + hormiguero);
        System.out.println("Hormiga obrera: " + TipoHormiga.OBRERA.getSimbolo());
    }

    /**
     * Colocamos espacios a todo el mapa y luego dibujamos el hormiguero y las hormigas.
     * Big O(n*m+o)
     * @param hormigas HashMap que contiene una clave y un objeto Hormiga como valor.
     */
    public void prepararMapa(HashMap<String, Hormiga> hormigas) {
        /*
        Volvemos a asignar vació al mapa entero como hicimos antes, y volvemos a colocar el hormiguero en el centro.
         */
        for (int i = 0; i < ANCHO; i++) {
            for (int j = 0; j < ALTO; j++) {
                mapa[i][j] = VACIO;
            }
        }
        this.mapa[hormiguero.getX()][hormiguero.getY()] = HORMIGUERO;
        /*
        Recorro el HashMap de hormigas colocándolas en el mapa en el proceso.
         */
        for (Hormiga hormiga : hormigas.values()) {
            mapa[hormiga.getPosicion().getY()][hormiga.getPosicion().getX()] = hormiga.getTipo().getSimbolo().charAt(0);
        }
    }
}
