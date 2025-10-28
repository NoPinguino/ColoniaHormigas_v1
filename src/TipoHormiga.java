public enum TipoHormiga {
    OBRERA("O","Hormiga obrera"),
    GUERRERA("G", "Hormiga guerrera"),
    REINA ("R","Hormiga reina");

    private final String simbolo;
    private final String nombre;

    //Constructor
    TipoHormiga(String simbolo, String nombre) {
        this.simbolo = simbolo;
        this.nombre = nombre;
    }

    /*
    Getter's, al ser final no usa setter's.
     */
    public String getNombre() {
        return this.nombre;
    }
    public String getSimbolo() {
        return this.simbolo;
    }
}
