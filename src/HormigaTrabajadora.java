public class HormigaTrabajadora extends Hormiga {
    /**
     *
     * @param id Identificador de la instancia HormigaTrabajadora.
     * @param posicionInicial Instancia de Posicion que corresponde a posX posY inicial.
     * @param simuladorColoniaHormigas Instancia del simulador.
     */
    public HormigaTrabajadora(String id, Posicion posicionInicial, SimuladorColoniaHormigas simuladorColoniaHormigas) {
        super(id, TipoHormiga.TRABAJADORA, posicionInicial, simuladorColoniaHormigas);
    }
}