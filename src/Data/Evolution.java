package Data;

public class Evolution {
    private int pokemonID;
    private EvolutionMethod evolutionMethod;
    private String parameter;

    public Evolution(int pokemonID, EvolutionMethod evolutionMethod, String parameter) {
        this.pokemonID = pokemonID;
        this.evolutionMethod = evolutionMethod;
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "Evolution{" +
                "pokemonID=" + pokemonID +
                ", evolutionMethod=" + evolutionMethod +
                ", parameter='" + parameter + '\'' +
                '}';
    }
}
