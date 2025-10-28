public class Posicion {
    private final int x, y;

    //Constructor
    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
    Getter's, al ser final no usa setter's.
     */
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    /**
     * Comprueba que una posición sea posible en los límites del hormiguero.
     * Big O(1)
     * @param maxX Valor máximo que puede adoptar X sin salir de límites.
     * @param maxY Valor máximo que puede adoptar Y sin salir de límites.
     * @return
     */
    public boolean dentroLimites(int maxX, int maxY) {
        return (this.x >= 0 && this.x <= maxX) && (this.y >= 0 && this.y <= maxY);
    }

    /**
     * Devuelve una nueva posición, sumandole las deltas de X/Y a la anterior.
     * Big O(1)
     * @param deltaX Incremento de la posición X
     * @param deltaY Incremento de la posición Y
     * @return
     */
    public Posicion mover(int deltaX, int deltaY) {
        return new Posicion(this.x + deltaX, this.y + deltaY);
    }
}
