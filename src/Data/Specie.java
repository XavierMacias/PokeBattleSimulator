package Data;

import java.util.ArrayList;
import org.apache.commons.collections4.MultiValuedMap;

public class Specie {
    private int id;
    private String name;
    private String displayName;
    private String description;
    private String category;

    private ArrayList<Type> types;
    private ArrayList<Ability> abilities;
    private Ability hiddenAbility;

    private double femaleRate;
    private int catchRate;
    private int hatchCycles;
    private double height, weight;

    private ArrayList<EggGroup> eggGroups;
    private LevelingRate levelingRate;
    private int baseExp, baseFriendship;
    private ArrayList<Integer> evs; // order is: HP, Attack, Defense, Special Attack, Special Defense, Speed

    private ArrayList<Integer> stats; // order is: HP, Attack, Defense, Special Attack, Special Defense, Speed
    private MultiValuedMap<Integer, Movement> moveset;
    private ArrayList<Movement> tutorMoves;
    private ArrayList<Movement> eggMoves;

    private ArrayList<Evolution> evolutions;

    public Specie(int id, String name, String displayName, String description, String category, ArrayList<Type> types, ArrayList<Ability> abilities,
                  Ability hiddenAbility, double femaleRate, int catchRate, int hatchCycles, double height, double weight, ArrayList<EggGroup> eggGroups,
                  LevelingRate levelingRate, int baseExp, int baseFriendship, ArrayList<Integer> evs, ArrayList<Integer> stats, MultiValuedMap<Integer,
                    Movement> moveset, ArrayList<Movement> tutorMoves, ArrayList<Movement> eggMoves, ArrayList<Evolution> evolutions) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.category = category;
        this.types = types;
        this.abilities = abilities;
        this.hiddenAbility = hiddenAbility;
        this.femaleRate = femaleRate;
        this.catchRate = catchRate;
        this.hatchCycles = hatchCycles;
        this.height = height;
        this.weight = weight;
        this.eggGroups = eggGroups;
        this.levelingRate = levelingRate;
        this.baseExp = baseExp;
        this.baseFriendship = baseFriendship;
        this.evs = evs;
        this.stats = stats;
        this.moveset = moveset;
        this.tutorMoves = tutorMoves;
        this.eggMoves = eggMoves;
        this.evolutions = evolutions;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getBaseFriendship() {
        return baseFriendship;
    }

    public double getFemaleRate() {
        return femaleRate;
    }

    public LevelingRate getLevelingRate() {
        return levelingRate;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public MultiValuedMap<Integer, Movement> getMoveset() {
        return moveset;
    }

    public int getStats(int index) {
        return stats.get(index);
    }

    @Override
    public String toString() {
        return "Specie{" +
                "id=" + id +
                ", name=" + name + '\n' +
                ", displayName=" + displayName + '\n' +
                ", description=" + description + '\n' +
                ", category=" + category + '\n' +
                ", types=" + types.toString() + '\n' +
                ", abilities=" + abilities.toString() + '\n' +
                ", hiddenAbility=" + (hiddenAbility != null ? hiddenAbility.toString() : "") + '\n' +
                ", femaleRate=" + femaleRate + '\n' +
                ", catchRate=" + catchRate + '\n' +
                ", hatchCycles=" + hatchCycles + '\n' +
                ", height=" + height + '\n' +
                ", weight=" + weight + '\n' +
                ", eggGroups=" + eggGroups.toString() + '\n' +
                ", levelingRate=" + levelingRate + '\n' +
                ", baseExp=" + baseExp + '\n' +
                ", baseFriendship=" + baseFriendship + '\n' +
                ", evs=" + evs.toString() + '\n' +
                ", stats=" + stats.toString() + '\n' +
                ", moveset=" + moveset.toString() + '\n' +
                ", tutorMoves=" + tutorMoves.toString() + '\n' +
                ", eggMoves=" + eggMoves.toString() + '\n' +
                ", evolutions=" + evolutions.toString() +
                '}';
    }
}
