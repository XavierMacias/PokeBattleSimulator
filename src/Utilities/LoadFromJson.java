package Utilities;

import Data.*;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Range;

import java.io.*;
import java.util.ArrayList;

public class LoadFromJson {
    public LoadFromJson() {}

    private JSONArray parseJSON(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        return new JSONArray(sb.toString());
    }

    public ArrayList<Type> loadTypes(String filename) {
        ArrayList<Type> types = new ArrayList<>();
        try {
            JSONArray jsonArray = parseJSON(filename);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject typeJSON = jsonArray.getJSONObject(i);

                ArrayList<Integer> debilities = new ArrayList<>();
                JSONArray debArr = typeJSON.getJSONArray("debilities");
                for (int j = 0; j < debArr.length(); j++) {
                    debilities.add(debArr.getInt(j));
                }

                ArrayList<Integer> resistances = new ArrayList<>();
                JSONArray resArr = typeJSON.getJSONArray("resistances");
                for (int j = 0; j < resArr.length(); j++) {
                    resistances.add(resArr.getInt(j));
                }

                ArrayList<Integer> inmunities = new ArrayList<>();
                JSONArray inmArr = typeJSON.getJSONArray("inmunities");
                for (int j = 0; j < inmArr.length(); j++) {
                    inmunities.add(inmArr.getInt(j));
                }

                Type type = new Type(typeJSON.getInt("id"), typeJSON.getString("name"), typeJSON.getString("displayName"),
                        debilities, resistances, inmunities);
                types.add(type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return types;
    }

    public ArrayList<Ability> loadAbilities(String filename) {
        ArrayList<Ability> abilities = new ArrayList<>();
        try {
            JSONArray jsonArray = parseJSON(filename);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject abilityJSON = jsonArray.getJSONObject(i);

                Ability ability = new Ability(abilityJSON.getInt("id"), abilityJSON.getString("name"), abilityJSON.getString("displayName"),
                        abilityJSON.getString("description"));
                abilities.add(ability);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return abilities;
    }

    public ArrayList<Movement> loadMovements(String filename) {
        ArrayList<Movement> moves = new ArrayList<>();
        try {
            JSONArray jsonArray = parseJSON(filename);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject moveJSON = jsonArray.getJSONObject(i);

                JSONObject typeJSON = moveJSON.getJSONObject("type");
                Type type = GlobalVariables.getInstance().getTypeFromId(typeJSON.getInt("id"));

                Movement move = new Movement(moveJSON.getInt("id"), moveJSON.getString("name"), moveJSON.getString("displayName"),
                        moveJSON.getString("description"), moveJSON.getInt("power"), moveJSON.getInt("accuracy"), moveJSON.getInt("pp"),
                        moveJSON.getInt("maxPp"), moveJSON.getInt("priority"), moveJSON.getInt("chanceEffect"),
                        moveJSON.getEnum(Category.class, "category"), type, moveJSON.getEnum(Target.class, "target"), moveJSON.getString("effect"),
                        moveJSON.getString("flags"));
                moves.add(move);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return moves;
    }

    public ArrayList<Specie> loadSpecies(String filename) {
        ArrayList<Specie> species = new ArrayList<>();
        try {
            JSONArray jsonArray = parseJSON(filename);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject moveJSON = jsonArray.getJSONObject(i);

                JSONArray typesJSON = moveJSON.getJSONArray("types");
                ArrayList<Type> types = new ArrayList<>();
                for (int j = 0; j < typesJSON.length(); j++) {
                    types.add(GlobalVariables.getInstance().getTypeFromId(typesJSON.getInt(j)));
                }

                JSONArray abilitiesJSON = moveJSON.getJSONArray("abilities");
                ArrayList<Ability> abilities = new ArrayList<>();
                for (int j = 0; j < abilitiesJSON.length(); j++) {
                    abilities.add(GlobalVariables.getInstance().getAbilityFromId(abilitiesJSON.getInt(j)));
                }

                int hdnAbilityID = moveJSON.getInt("hiddenAbility");
                Ability hiddenAbility = GlobalVariables.getInstance().getAbilityFromId(hdnAbilityID);

                JSONArray eggGroupsJSON = moveJSON.getJSONArray("eggGroups");
                ArrayList<EggGroup> eggGroups = new ArrayList<>();
                for (int j = 0; j < eggGroupsJSON.length(); j++) {
                    eggGroups.add(EggGroup.valueOf(eggGroupsJSON.getString(j)));
                }

                JSONArray evsJSON = moveJSON.getJSONArray("evs");
                ArrayList<Integer> evs = new ArrayList<>();
                for (int j = 0; j < evsJSON.length(); j++) {
                    evs.add(evsJSON.getInt(j));
                }

                JSONArray statsJSON = moveJSON.getJSONArray("stats");
                ArrayList<Integer> stats = new ArrayList<>();
                for (int j = 0; j < statsJSON.length(); j++) {
                    stats.add(statsJSON.getInt(j));
                }

                JSONArray movesetJSON = moveJSON.getJSONArray("moveset");
                MultiValuedMap<Integer, Movement> moveset = new ArrayListValuedHashMap<>();
                for (int j = 0; j < movesetJSON.length(); j++) {
                    int level = Integer.parseInt(movesetJSON.getJSONObject(j).keys().next());
                    moveset.put(level,GlobalVariables.getInstance().getMovementFromId(movesetJSON.getJSONObject(j).getInt(String.valueOf(level))));
                }


                JSONArray tutorMovesJSON = moveJSON.getJSONArray("tutorMoves");
                ArrayList<Movement> tutorMoves = new ArrayList<>();
                for (int j = 0; j < tutorMovesJSON.length(); j++) {
                    tutorMoves.add(GlobalVariables.getInstance().getMovementFromId(tutorMovesJSON.getInt(j)));
                }

                JSONArray eggMovesJSON = moveJSON.getJSONArray("eggMoves");
                ArrayList<Movement> eggMoves = new ArrayList<>();
                for (int j = 0; j < eggMovesJSON.length(); j++) {
                    eggMoves.add(GlobalVariables.getInstance().getMovementFromId(eggMovesJSON.getInt(j)));
                }

                JSONArray evolutionsJSON = moveJSON.getJSONArray("evolutions");
                ArrayList<Evolution> evolutions = new ArrayList<>();
                for (int j = 0; j < evolutionsJSON.length(); j++) {
                    //System.out.println(evolutionsJSON.getJSONObject(j));
                    Evolution evo = new Evolution(evolutionsJSON.getJSONObject(j).getInt("pokemonID"),
                            evolutionsJSON.getJSONObject(j).getEnum(EvolutionMethod.class, "evolutionMethod"),
                            evolutionsJSON.getJSONObject(j).getString("parameter"));
                    evolutions.add(evo);
                }

                Specie specie = new Specie(moveJSON.getInt("id"), moveJSON.getString("name"), moveJSON.getString("displayName"),
                        moveJSON.getString("description"), moveJSON.getString("category"), types, abilities, hiddenAbility,
                        moveJSON.getDouble("femaleRate"), moveJSON.getInt("catchRate"), moveJSON.getInt("hatchCycles"),
                        moveJSON.getDouble("height"), moveJSON.getDouble("weight"), eggGroups,
                        moveJSON.getEnum(LevelingRate.class, "levelingRate"), moveJSON.getInt("baseExp"),
                        moveJSON.getInt("baseFriendship"), evs, stats, moveset, tutorMoves, eggMoves, evolutions);
                species.add(specie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return species;
    }

    public void changeJSON(String filename, ArrayList<String> movesNames, ArrayList<Integer> chances, ArrayList<String> effects) {
        try {
            JSONArray jsonArray = parseJSON(filename);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject move = jsonArray.getJSONObject(i);
                move.put("effect", "000");
                if (movesNames.contains(move.getString("name"))) {
                    int index = movesNames.indexOf(move.getString("name"));
                    move.put("chanceEffect", chances.get(index));
                    move.put("effect", effects.get(index));
                }
            }

            try (FileWriter file = new FileWriter(filename)) {
                file.write(jsonArray.toString(2));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSpecie(String filename, int id, String name, String internalName, String category, ArrayList<Integer> typeID,
                          ArrayList<Integer> abilitiesIDs, int hdnAbilityID, double femaleRatio, String catchRate, ArrayList<EggGroup> eggGroups2,
                          String hatchTime, String height, String weight, String baseExp, String levelingRate, ArrayList<Integer> evs,
                          String baseFriendship, ArrayList<Integer> stats, ArrayList<Integer> moveset, ArrayList<Integer> movesTutor,
                          ArrayList<Integer> eggMoves, String description) {
        try {
            JSONArray jsonArray = parseJSON(filename);

            JSONObject JSONspecie = new JSONObject();

            ArrayList<Evolution> evo = new ArrayList<>();

            JSONspecie.put("femaleRate", femaleRatio);
            JSONspecie.put("displayName", name);
            JSONspecie.put("category", category);
            JSONspecie.put("catchRate", Integer.parseInt(catchRate));
            JSONspecie.put("description", description);
            JSONspecie.put("hatchCycles", Integer.parseInt(hatchTime));
            JSONspecie.put("height", Double.parseDouble(height));
            JSONspecie.put("weight", Double.parseDouble(weight));
            JSONspecie.put("baseExp", Integer.parseInt(baseExp));
            JSONspecie.put("types", typeID);
            JSONspecie.put("abilities", abilitiesIDs);
            JSONspecie.put("hiddenAbility", hdnAbilityID);
            JSONspecie.put("eggGroups", eggGroups2);
            JSONspecie.put("evs", evs);
            JSONspecie.put("name", internalName);
            JSONspecie.put("id", id);
            JSONspecie.put("baseFriendship", Integer.parseInt(baseFriendship));
            JSONspecie.put("levelingRate", LevelingRate.valueOf(levelingRate));
            JSONspecie.put("stats", stats);
            JSONspecie.put("evolutions", evo);
            JSONspecie.put("eggMoves", eggMoves);
            JSONspecie.put("tutorMoves", movesTutor);

            JSONArray movesets = new JSONArray();

            for (int i = 0; i < moveset.size(); i+=2) {
                JSONObject JSONmove = new JSONObject();
                JSONmove.put(moveset.get(i).toString(), moveset.get(i+1));
                movesets.put(JSONmove);
            }

            JSONspecie.put("moveset", movesets);

            jsonArray.put(JSONspecie);

            try (FileWriter file = new FileWriter(filename)) {
                file.write(jsonArray.toString(2));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
