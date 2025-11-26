public class HormigaObrera extends Hormiga{
    /**
     * Constructor de la clase hormiga obrera.
     * @param id Identificador de instancia HormigaObrera.
     * @param posicionInicial Instancia de Posicion que corresponde a posX posY inicial.
     * @param simuladorColoniaHormigas Instancia del simulador.
     */
    public HormigaObrera(String id, Posicion posicionInicial, SimuladorColoniaHormigas simuladorColoniaHormigas) {
        super(id, TipoHormiga.OBRERA, posicionInicial, simuladorColoniaHormigas);
    }
}