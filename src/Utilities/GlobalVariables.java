package Utilities;

import Data.Ability;
import Data.Type;

import java.util.ArrayList;

public class GlobalVariables {
    private static GlobalVariables instance;

    // data arrays
    private ArrayList<Type> pokemonTypes;
    private ArrayList<Ability> pokemonAbilities;

    private GlobalVariables() {
        pokemonTypes = new ArrayList<>();
        pokemonAbilities = new ArrayList<>();
    }

    public static GlobalVariables getInstance() {
        if (instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }

    public ArrayList<Type> getPokemonTypes() {
        return pokemonTypes;
    }

    public ArrayList<Ability> getPokemonAbilities() {
        return pokemonAbilities;
    }

    public void setPokemonTypes(ArrayList<Type> types) {
        this.pokemonTypes = types;
    }

    public void setPokemonAbilities(ArrayList<Ability> abilities) {
        this.pokemonAbilities = abilities;
    }

    public void printAbilities() {
        for (int i = 0; i < pokemonAbilities.size(); i++) {
            pokemonAbilities.get(i).printAbility();
            System.out.println("\n");
        }
    }

    public Type getTypeFromId(int id) {
        for (Type type : pokemonTypes) {
            if (type.getId() == id) { return type; }
        }

        return null;
    }
}
