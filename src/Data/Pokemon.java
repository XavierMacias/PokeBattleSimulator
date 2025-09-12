package Data;

import Utilities.Utilities;
import org.apache.commons.collections4.MultiValuedMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Pokemon {
    private Specie specie;
    private int level;
    private int experience;
    private Nature nature;

    private ArrayList<Integer> evs;
    private ArrayList<Integer> ivs = new ArrayList<>(Collections.nCopies(6, 0));
    private ArrayList<Integer> stats = new ArrayList<>();
    private int currentHP;

    private int gender; // 0 male, 1 female, -1 no gender
    private Ability ability;
    private ArrayList<Movement> movements = new ArrayList<>();

    private boolean isShiny;
    private Status status;
    private int friendship;

    Utilities utilities = new Utilities();

    public Pokemon(Specie specie, int level) {
        this.specie = specie;
        this.level = level;

        this.evs = new ArrayList<>(Collections.nCopies(6, 0));
        for(int i=0;i<6;i++) {
            this.ivs.set(i, utilities.getRandomNumBetween(0, 31));
        }

        this.nature = utilities.randomEnum(Nature.class);
        this.status = Status.FINE;
        this.friendship = specie.getBaseFriendship();
        this.experience = getExperienceLevel(level);

        if(specie.getFemaleRate() == -1) {
            this.gender = -1;
        } else {
            this.gender = Math.random() < (specie.getFemaleRate() / 100.0) ? 1 : 0;
        }

        this.ability = specie.getAbilities().get(utilities.getRandomNumBetween(0, specie.getAbilities().size()-1));

        this.isShiny = utilities.getRandomNumBetween(1, 4096) == 4095;
        this.movements = getInitialMoves();

        this.stats = calcStats(this.level);
        this.currentHP = this.stats.getFirst();
    }


    private ArrayList<Movement> getInitialMoves() {
        MultiValuedMap<Integer, Movement> moveset = specie.getMoveset();
        ArrayList<Movement> movesetInit = new ArrayList<>();

        for(int i=1;i<=level;i++) {
            Collection<Movement> movesLevel = moveset.get(i);
            for (Movement move : movesLevel) {
                if(movesetInit.size() < 4) {
                    movesetInit.add(move);
                } else if (movesetInit.size() == 4 && utilities.getRandomNumBetween(0, 1) == 0) {
                    movesetInit.removeFirst();
                    movesetInit.add(move);
                }
            }
        }

        return movesetInit;
    }

    private double getNatMult(Nature nat, int stat) {
        double mult = 1.0;
        if(stat == 1) { // ATTACK
            if(nat.equals(Nature.LONELY) || nat.equals(Nature.BRAVE) || nat.equals(Nature.ADAMANT) || nat.equals(Nature.NAUGHTY)) {
                return 1.1;
            } else if (nat.equals(Nature.BOLD) || nat.equals(Nature.TIMID) || nat.equals(Nature.MODEST) || nat.equals(Nature.CALM)) {
                return 0.9;
            }
        } else if(stat == 2) { // DEFENSE
                if(nat.equals(Nature.BOLD) || nat.equals(Nature.RELAXED) || nat.equals(Nature.IMPISH) || nat.equals(Nature.LAX)) {
                return 1.1;
            } else if (nat.equals(Nature.LONELY) || nat.equals(Nature.HASTY) || nat.equals(Nature.MILD) || nat.equals(Nature.GENTLE)) {
                return 0.9;
            }
        } else if(stat == 3) { // SPECIAL ATTACK
            if(nat.equals(Nature.MODEST) || nat.equals(Nature.MILD) || nat.equals(Nature.QUIET) || nat.equals(Nature.RASH)) {
                return 1.1;
            } else if (nat.equals(Nature.ADAMANT) || nat.equals(Nature.IMPISH) || nat.equals(Nature.JOLLY) || nat.equals(Nature.CAREFUL)) {
                return 0.9;
            }
        } else if(stat == 4) { // SPECIAL DEFENSE
            if(nat.equals(Nature.CALM) || nat.equals(Nature.GENTLE) || nat.equals(Nature.SASSY) || nat.equals(Nature.CAREFUL)) {
                return 1.1;
            } else if (nat.equals(Nature.NAUGHTY) || nat.equals(Nature.LAX) || nat.equals(Nature.NAIVE) || nat.equals(Nature.RASH)) {
                return 0.9;
            }
        } else if(stat == 5) { // SPEED
            if(nat.equals(Nature.TIMID) || nat.equals(Nature.HASTY) || nat.equals(Nature.JOLLY) || nat.equals(Nature.NAIVE)) {
                return 1.1;
            } else if (nat.equals(Nature.BRAVE) || nat.equals(Nature.RELAXED) || nat.equals(Nature.QUIET) || nat.equals(Nature.SASSY)) {
                return 0.9;
            }
        }

        return mult;
    }

    private ArrayList<Integer> calcStats(int lvl) {
        ArrayList<Integer> stat = new ArrayList<>();
        for(int i=0;i<6;i++) {
            int ev = evs.get(i)/4;
            if(i==0) {
                int hp = ((((2*specie.getStats(i))+ivs.get(i)+ev)*lvl)/100) + lvl + 10;
                stat.add(hp);
            } else {
                double nat = getNatMult(nature, i);
                int num = ((2*specie.getStats(i))+ivs.get(i)+ev)*lvl;
                int div = num/100;
                int st = (int) ((div+5)*nat);
                stat.add(st);
            }
        }

        return stat;
    }

    private double getP(int rest) {
        if(rest == 0) return 0;
        if(rest == 1) return 0.008;
        return 0.014;
    }

    private int getExperienceLevel(int newLevel) {
        if(newLevel == 1) { return 0; }

        int pow = (int) Math.pow(newLevel, 3);
        if(specie.getLevelingRate().equals(LevelingRate.FAST)) {
            return (4*pow)/5;
        } else if (specie.getLevelingRate().equals(LevelingRate.MEDIUMFAST)) {
            return pow;
        } else if (specie.getLevelingRate().equals(LevelingRate.SLOW)) {
            return (5*pow)/4;
        } else if (specie.getLevelingRate().equals(LevelingRate.MEDIUMSLOW)) {
            return (int) ((1.2*pow)-(15*Math.pow(newLevel, 2))+(100*newLevel)-140);
        } else if (specie.getLevelingRate().equals(LevelingRate.ERRATIC)) {
            if(newLevel < 50) {
                return ((pow*(100-newLevel))/50);
            } else if (newLevel < 68) {
                return ((pow*(150-newLevel))/100);
            } else if (newLevel < 98) {
                int lev = newLevel/3;
                return (int) (pow*(1.274-0.02*lev-(getP(newLevel%3))));
            } else {
                return ((pow*(160-newLevel))/100);
            }
        } else if (specie.getLevelingRate().equals(LevelingRate.FLUCTUATING)) {
            if(newLevel <= 15) {
                return ((pow*(((newLevel + 1) /3)+24))/50);
            } else if (newLevel <= 36) {
                return ((pow*(newLevel+14))/50);
            } else {
                return ((pow*((newLevel/2)+32))/50);
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "specie=" + specie.getName() +
                ",\n level=" + level +
                ",\n experience=" + experience +
                ",\n nature=" + nature +
                ",\n evs=" + evs +
                ",\n ivs=" + ivs +
                ",\n stats=" + stats +
                ",\n currentHP=" + currentHP +
                ",\n gender=" + gender +
                ",\n ability=" + ability.getName() +
                ",\n movements=" + movements.toString() +
                ",\n isShiny=" + isShiny +
                ",\n status=" + status +
                ",\n friendship=" + friendship +
                '}';
    }
}
