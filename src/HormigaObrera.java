public class HormigaObrera extends Hormiga{
    /**
     * Constructor: inicializa a la hormiga como tipo OBRERA
     * @param id Atributo id heredado de Hormiga
     * @param posicionInicial Atributo PosicionInicial heredado de Hormiga
     */
    public HormigaObrera(String id, Posicion posicionInicial) {
        super(id, TipoHormiga.OBRERA, posicionInicial);
    }
}
