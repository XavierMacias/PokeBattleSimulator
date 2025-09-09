package Utilities;

import Data.Ability;
import Data.Movement;
import Data.Specie;
import Data.Type;

import java.util.ArrayList;
import java.util.Objects;

public class GlobalVariables {
    private static GlobalVariables instance;

    // data arrays
    private ArrayList<Type> pokemonTypes;
    private ArrayList<Ability> pokemonAbilities;
    private ArrayList<Movement> pokemonMovements;
    private ArrayList<Specie> pokemonSpecies;

    private GlobalVariables() {
        pokemonTypes = new ArrayList<>();
        pokemonAbilities = new ArrayList<>();
        pokemonMovements = new ArrayList<>();
        pokemonSpecies = new ArrayList<>();
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

    public ArrayList<Movement> getPokemonMovements() {
        return pokemonMovements;
    }

    public ArrayList<Specie> getPokemonSpecies() {
        return pokemonSpecies;
    }

    public void setPokemonTypes(ArrayList<Type> types) {
        this.pokemonTypes = types;
    }

    public void setPokemonAbilities(ArrayList<Ability> abilities) {
        this.pokemonAbilities = abilities;
    }

    public void setPokemonMovements(ArrayList<Movement> pokemonMovements) {
        this.pokemonMovements = pokemonMovements;
    }

    public void setPokemonSpecies(ArrayList<Specie> pokemonSpecies) {
        this.pokemonSpecies = pokemonSpecies;
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

    public Ability getAbilityFromId(int id) {
        for (Ability ability : pokemonAbilities) {
            if (ability.getId() == id) { return ability; }
        }

        return null;
    }

    public Type getTypeFromName(String name) {
        for (Type type : pokemonTypes) {
            if (Objects.equals(type.getName(), name)) { return type; }
        }
        return null;
    }

    public Ability getAbilityFromName(String name) {
        for (Ability ability : pokemonAbilities) {
            if (Objects.equals(ability.getName(), name)) { return ability; }
        }
        return null;
    }

    public Movement getMovementFromName(String name) {
        for (Movement movement : pokemonMovements) {
            if (Objects.equals(movement.getName(), name)) { return movement; }
        }
        return null;
    }

    public int getIDMovementFromName(String name) {
        for (Movement movement : pokemonMovements) {
            if (Objects.equals(movement.getName(), name)) { return movement.getId(); }
        }
        return -1;
    }

    public Movement getMovementFromId(int id) {
        for (Movement movement : pokemonMovements) {
            if (movement.getId() == id) { return movement; }
        }

        return null;
    }

    public Specie getSpecieFromId(int id) {
        for (Specie specie : pokemonSpecies) {
            if (specie.getId() == id) { return specie; }
        }

        return null;
    }
}
