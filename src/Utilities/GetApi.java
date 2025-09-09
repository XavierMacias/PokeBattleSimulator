package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Data.*;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class GetApi {

    private final String LANGUAGE = "en";

    public GetApi() {}

    public StringBuilder callApi(String endpoint) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();

            if (status == 200) { // ok status
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int returnArray(String url) {
        StringBuilder response = callApi(url);
        if (response != null) {
            JSONObject json = new JSONObject(response.toString());
            return json.getInt("count");
        } else {
            return 0;
        }
    }

    public JSONObject returnObject(String url) {
        StringBuilder result = callApi(url);
        if (result != null) {
            StringBuilder response = callApi(url);
            return new JSONObject(response.toString());
        } else {
            return null;
        }
    }

    public ArrayList<Integer> getDamageRelations(String damageRel, String relation) {
        JSONObject json = new JSONObject(damageRel);
        JSONArray noDamageFrom = json.getJSONArray(relation);
        ArrayList<Integer> types = new ArrayList<>();

        for (int i = 0; i < noDamageFrom.length(); i++) {
            JSONObject obj = noDamageFrom.getJSONObject(i);
            String[] parts = obj.getString("url").split("/");
            String number = parts[parts.length - 1].isEmpty() ? parts[parts.length - 2] : parts[parts.length - 1];
            types.add(Integer.parseInt(number));
        }

        return types;
    }

    public String getName(JSONObject json) {
        JSONArray names = json.getJSONArray("names");

        for (int i = 0; i < names.length(); i++) {
            JSONObject obj = names.getJSONObject(i);
            JSONObject lang = obj.getJSONObject("language");
            if(LANGUAGE.equals(lang.getString("name"))) {
                return obj.getString("name");
            }
        }
        return "";
    }

    public String getLastDescription(JSONObject json, String nameList, String subName) {
        JSONArray names = json.getJSONArray(nameList);

        for (int i = names.length()-1; i >= 0; i--) {
            JSONObject obj = names.getJSONObject(i);
            JSONObject lang = obj.getJSONObject("language");
            if(LANGUAGE.equals(lang.getString("name"))) {
                return obj.getString(subName);
            }
        }

        return "";
    }

    public void getTypes() throws IOException, InterruptedException {
        int count = returnArray("https://pokeapi.co/api/v2/type");

        for (int i = 1; i < count; i++) {
            JSONObject typeJSON = returnObject("https://pokeapi.co/api/v2/type/" + i + "/");
            if(typeJSON != null) {
                String damage_relations = typeJSON.get("damage_relations").toString();

                Type type = new Type(Integer.parseInt(typeJSON.get("id").toString()), typeJSON.get("name").toString().toUpperCase(), getName(typeJSON),
                        getDamageRelations(damage_relations, "double_damage_from"), getDamageRelations(damage_relations, "half_damage_from"),
                        getDamageRelations(damage_relations, "no_damage_from"));

                GlobalVariables.getInstance().getPokemonTypes().add(type);
            }
        }
    }

    public void getAbilities() throws IOException, InterruptedException {
        int count = returnArray("https://pokeapi.co/api/v2/ability?offset=0&limit=20");

        for (int i = 1; i <= count; i++) {
            JSONObject abilityJSON = returnObject("https://pokeapi.co/api/v2/ability/" + i + "/");
            if(abilityJSON != null) {
                String description = getLastDescription(abilityJSON, "flavor_text_entries", "flavor_text");

                Ability ability = new Ability(Integer.parseInt(abilityJSON.get("id").toString()), abilityJSON.get("name").toString().toUpperCase(),
                        getName(abilityJSON), description);

                GlobalVariables.getInstance().getPokemonAbilities().add(ability);
            }
        }
    }

    private String normalizeName(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        String noAccents = normalized.replaceAll("\\p{M}", "");

        String noSpaces = noAccents.replaceAll(" ", "-");
        String clean = noSpaces.replaceAll("[^A-Za-z0-9-]", "");
        return clean.toUpperCase();
    }

    private String normalizeNameNoLine(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        String noAccents = normalized.replaceAll("\\p{M}", "");

        String noSpaces = noAccents.replaceAll(" ", "");
        String clean = noSpaces.replaceAll("[^A-Za-z0-9-]", "");
        return clean.toUpperCase();
    }

    private Target getTarget(String text) {
        if (Objects.equals(text, "1 Other")) {
            return Target.FOE;
        }
        if (Objects.equals(text, "1 Ally")) {
            return Target.ALLY;
        }
        if (Objects.equals(text, "Self/Ally")) {
            return Target.SELFORALLY;
        }
        if (Objects.equals(text, "All Allies (Except Self)")) {
            return Target.ALLALLIES;
        }
        if (Objects.equals(text, "1 Random")) {
            return Target.RANDOMFOE;
        }

        return Target.valueOf(normalizeNameNoLine(text));
    }

    private String getFlags(Elements flagsList) {
        String flags = "";

        for (int i=0; i<flagsList.size(); i++) {
            if(i==0 && flagsList.get(i).text().equals("Makes contact")) {
                flags += "a";
            }
            if(i==1 && flagsList.get(i).text().equals("Affected by Protect")) {
                flags += "b";
            }
            if(i==2 && !flagsList.get(i).text().equals("Not affected by Magic Coat")) {
                flags += "c";
            }
            if(i==3 && flagsList.get(i).text().equals("Affected by Snatch")) {
                flags += "d";
            }
            if(i==4 && flagsList.get(i).text().equals("Affected by Mirror Move")) {
                flags += "e";
            }
            if(i==5 && flagsList.get(i).text().equals("Affected by King's Rock")) {
                flags += "f";
            }
            if(i==6 && flagsList.get(i).text().equals("Is a sound-based move")) {
                flags += "g";
            }
        }

        return flags;
    }

    private void getMove(int id, String url) throws IOException {
        Document doc = Jsoup.connect("https://bulbapedia.bulbagarden.net" + url).get();
        //System.out.println(id);

        Element infobox = doc.selectFirst("table.infobox");
        String name, description = "", flags = "";

        if (infobox != null) {
            Elements tds = infobox.select("td");

            Elements nameText = tds.getFirst().select("font[size=4]");
            name = Objects.requireNonNull(nameText.first()).text();

            Elements links = tds.select("a[title*=(type] span");
            Element td = links.getFirst().closest("td");
            int indexType = tds.indexOf(td);

            Type type = GlobalVariables.getInstance().getTypeFromName(normalizeName(links.getFirst().text()));

            String cat = normalizeName(tds.get(indexType+1).text());
            Category category = Category.valueOf(cat);

            String power = tds.get(indexType+3).text().replaceAll("[*?¿!¡%#@/&$·()=|]", "");
            if (power.contains("—")) {
                power = "0";
            } else if (power.contains("Varie")) {
                power = "1";
            }

            String accuracy = tds.get(indexType+4).text().replace("%", "");
            if (accuracy.contains("—")) {
                accuracy = "0";
            }

            String pps = tds.get(indexType+2).text();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(pps);
            int pp = 0, maxpp = 0;
            if (matcher.find()) {
                pp = Integer.parseInt(matcher.group());
            }
            if (matcher.find()) {
                maxpp = Integer.parseInt(matcher.group());
            }

            Element listFlags = tds.get(indexType+6);

            Element table = doc.selectFirst("table[width=60%]");
            if (table != null) {
                Elements tdsTable = table.select("td");
                if (!tdsTable.getLast().text().contains("It's recommended that this move is forgotten")) {
                    description = tdsTable.getLast().text();
                } else if(tdsTable.size() >= 3) {
                    description = tdsTable.get(tdsTable.size()-3).text();
                }
            }

            String prior = tds.get(indexType+5).text().replaceAll("[−–]", "-");
            int priority = Integer.parseInt(prior.replaceAll("[*#@!¡?¿/&%$·|()=]", ""));

            Target target = getTarget(tds.get(indexType+14).text().split(":")[0]);

            Movement movement = new Movement(id, normalizeName(name), name, description, Integer.parseInt(power), Integer.parseInt(accuracy), pp,
                    maxpp, priority, 0, category, type, target, "", getFlags(listFlags.select("ul li")));

            GlobalVariables.getInstance().getPokemonMovements().add(movement);

        }
    }

    public void getMoves() throws IOException {
        Document doc = Jsoup.connect("https://bulbapedia.bulbagarden.net/wiki/List_of_moves").get();
        Element tableMoves = doc.selectFirst("table[width=100%]");
        if (tableMoves != null) {
            Elements rows = tableMoves.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Elements cells = rows.get(i).select("td");
                if (cells.size() > 1) {
                    String index = cells.getFirst().text();
                    Element moveName = cells.get(1);
                    Element link = moveName.selectFirst("a[title]");
                    if (link != null) {
                        String url = link.attr("href");
                        getMove(Integer.parseInt(index), url);
                    }
                }
            }
        }
    }

    private String getCode(String effect) {
        if(effect.equals("Varies with held item. Not considered an additional effect by Sheer Force.")) {
            return "001";
        } else if(effect.equals("Traps the target.")) {
            return "002";
        } else if(effect.equals("Special: Inflicts varying conditions depending on the battle environment. See Secret Power.")) {
            return "003";
        } else if(effect.equals("Reduces the PP of the target's most recently used move by 3.")) {
            return "004";
        } else if(effect.equals("Raises the user's Speed by 1 stage.")) {
            return "005";
        } else if(effect.equals("Raises the user's Special Attack by 1 stage.")) {
            return "006";
        } else if(effect.equals("Raises the user's Defense by 2 stages.")) {
            return "007";
        } else if(effect.equals("Raises the user's Defense by 1 stage.")) {
            return "008";
        } else if(effect.equals("Raises the user's Attack by 1 stage.")) {
            return "009";
        } else if(effect.equals("Raises one of the user's stats (Attack, Defense, or Speed) by 1 stage. The boosted stat depends on the Tatsugiri's form. See Order Up.")) {
            return "00A";
        } else if(effect.equals("Raises all of the user's stats (Attack, Defense, Special Attack, Special Defense, and Speed) by 1 stage.")) {
            return "00B";
        } else if(effect.equals("Prevents the target from using sound-based moves.")) {
            return "00C";
        } else if(effect.equals("Prevents the target from recovering HP for 2 turns. Can also hit the substitute.")) {
            return "00D";
        } else if(effect.equals("Lowers the target's Speed by 1 stage.")) {
            return "00E";
        } else if(effect.equals("Lowers the target's Special Defense by 2 stages.")) {
            return "00F";
        } else if(effect.equals("Lowers the target's Special Defense by 1 stage.")) {
            return "010";
        } else if(effect.equals("Lowers the target's Special Attack by 1 stage.")) {
            return "011";
        } else if(effect.equals("Lowers the target's Defense by 1 stage.")) {
            return "012";
        } else if(effect.equals("Lowers the target's Defense by 1 stage or inflicts flinch on the target.")) {
            return "013";
        } else if(effect.equals("Lowers the target's Attack by 1 stage.")) {
            return "014";
        } else if(effect.equals("Lowers the target's accuracy by 1 stage.")) {
            return "015";
        } else if(effect.equals("Inflicts sleep on the target.")) {
            return "016";
        } else if(effect.equals("Inflicts poison, paralysis, or sleep on the target.")) {
            return "017";
        } else if(effect.equals("Inflicts poison on the target.")) {
            return "018";
        } else if(effect.equals("Inflicts paralysis on the target.")) {
            return "019";
        } else if(effect.equals("Inflicts freeze on the target.")) {
            return "01A";
        } else if(effect.equals("Inflicts flinch or paralysis on the target.")) {
            return "01B";
        } else if(effect.equals("Inflicts flinch or freeze on the target.")) {
            return "01C";
        } else if(effect.equals("Inflicts flinch or burn on the target.")) {
            return "01D";
        } else if(effect.equals("Inflicts flinch on the target.")) {
            return "01E";
        } else if(effect.equals("Inflicts confusion on the target.")) {
            return "01F";
        } else if(effect.equals("Inflicts burn, freeze, or paralysis on the target.")) {
            return "020";
        } else if(effect.equals("Inflicts burn on the target.")) {
            return "021";
        } else if(effect.equals("Inflicts burn on targets with increased stats.")) {
            return "022";
        } else if(effect.equals("Inflicts badly poisoned on the target.") || effect.equals("Inflicts bad poison on the target.")) {
            return "023";
        } else if(effect.equals("Inflicts 1/8 of the target's maximum HP as damage per turn; if the target is Water or Steel, it inflicts 1/4 of the target's max HP as damage.")) {
            return "024";
        } else if(effect.equals("If the target has a burn, cures that burn.")) {
            return "025";
        } else if(effect.equals("Creates Stealth Rock on the target's side of the field.")) {
            return "026";
        } else if(effect.equals("Creates Spikes on the target's side of the field.")) {
            return "027";
        } else if(effect.equals("Creates Psychic Terrain.")) {
            return "028";
        } else if(effect.equals("Causes the target's Speed to be lowered by 1 stage each turn for 3 turns.")) {
            return "029";
        }

        return "000";
    }

    public void getEffects() throws IOException {
        LoadFromJson lfj = new LoadFromJson();
        Document doc = Jsoup.connect("https://bulbapedia.bulbagarden.net/wiki/Additional_effect").get();
        Element tableEffects = doc.selectFirst("table[width=100%]");

        ArrayList<String> effects = new ArrayList<>();
        ArrayList<Integer> chances = new ArrayList<>();
        ArrayList<String> moveNames = new ArrayList<>();

        if (tableEffects != null) {
            Elements rows = tableEffects.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Elements cells = rows.get(i).select("td");
                if (cells.size() > 3) {
                    String move = cells.getFirst().text();
                    String chanceText = cells.get(3).text();
                    String effect = cells.get(4).text();

                    if(chanceText.equals("Varies")) chanceText = "0";
                    int chance = Integer.parseInt(chanceText.replaceAll("%", ""));

                    moveNames.add(normalizeName(move));
                    chances.add(chance);
                    effects.add(getCode(effect));
                }
            }
        }
        lfj.changeJSON("json/movements.json", moveNames, chances, effects);
    }

    public void getSpecies() throws IOException {
        LoadFromJson lfj = new LoadFromJson();
        Document doc = Jsoup.connect("https://bulbapedia.bulbagarden.net/wiki/Caterpie_(Pokémon)").get();

        Element tablePoke = doc.selectFirst("table.roundy.infobox");

        if (tablePoke != null) {
            //System.out.println(tablePoke.outerHtml());
            String name = Objects.requireNonNull(tablePoke.selectFirst("big b")).text();

            String type = Objects.requireNonNull(tablePoke.select("a[title$='(type)']").first()).text();

            Elements abilities = tablePoke.select("a[title$='(Ability)']");
            String ability1 = normalizeName(abilities.get(0).text());
            String ability2 = abilities.size() > 2 ? normalizeName(abilities.get(3).text()) : "";

            Elements tds = tablePoke.select("td");
            //for (int i = 1; i < tds.size(); i++) {
            //    System.out.println(i + ": " + tds.get(i).text() + "\n");
            //}

            String[] partsCat = tds.get(2).text().split(" ");
            String category = partsCat[1];

            String genderRatio = tds.get(43).text();
            double femaleRatio = 0.0;
            double maleRatio = 0.0;
            if(genderRatio.equals("100% female")) {
                femaleRatio = 100.0;
            } else if(genderRatio.equals("Gender unknown")) {
                femaleRatio = -1;
            } else {
                Pattern p = Pattern.compile("(\\d+(?:\\.\\d+)?)%");
                Matcher m = p.matcher(genderRatio);
                if (m.find()) maleRatio = Double.parseDouble(m.group(1));
                if (m.find()) femaleRatio = Double.parseDouble(m.group(1));
            }

            String catchRate = tablePoke.select("a[title='Catch rate']").parents().next().select("td").first().ownText();

            String eggGroups = tds.get(48).text();
            String[] parts = eggGroups.split("and");
            String eggGroup1 = normalizeNameNoLine(parts[0].trim());
            String eggGroup2 = (parts.length > 1) ? normalizeNameNoLine(parts[1].trim()) : "";
            if(eggGroup1.equals("NOEGGSDISCOVERED")) {
                eggGroup1 = "UNDISCOVERED";
            }

            String hatchTime = tds.get(50).text().replaceAll("\\D+", "");

            String height = tds.get(53).text().replaceAll("[^0-9.]", "");;

            String weight = tds.get(66).text().replaceAll("[^0-9.]", "");;

            String baseExp = tds.get(83).text().replaceAll("\\D+", "");

            String levelingRate = normalizeNameNoLine(tds.get(85).text());

            String baseFriendship = tds.get(124).text();

            ArrayList<Integer> evs = new ArrayList<>();
            for (int i = 89; i < 95; i++) {
                evs.add(Integer.parseInt(tds.get(i).text().replaceAll("\\D+", "")));
            }

            Element statsTable = doc.selectFirst("table[style=background: #91A119; border-radius: 10px; -moz-border-radius: 10px; -webkit-border-radius: 10px; -khtml-border-radius: 10px; -icab-border-radius: 10px; -o-border-radius: 10px;; border: 3px solid #5E6910; border-collapse: separate; white-space: nowrap]");
            if(statsTable != null) {
                ArrayList<Integer> stats = new ArrayList<>();

                Elements rows = statsTable.select("tr");
                for (Element row : rows) {
                    Element th = row.selectFirst("th");
                    if (th != null) {
                        Elements divs = th.select("div");
                        if (divs.size() == 2) {
                            int statValue = Integer.parseInt(divs.get(1).text().trim());
                            stats.add(statValue);
                        }
                    }
                }

                stats.removeLast();

                ArrayList<Integer> moveset = new ArrayList<>();
                ArrayList<Integer> movesTutor = new ArrayList<>();
                ArrayList<Integer> eggMoves = new ArrayList<>();

                Elements movesTables = doc.select("table.roundy table.sortable");
                Elements rowsMoveset = movesTables.getFirst().select("tbody > tr");
                for (Element row : rowsMoveset) {
                    Elements cells = row.select("td");
                    if (cells.size() == 7) {
                        String level = cells.get(0).ownText().trim();

                        Element moveCell = cells.get(1);
                        String moveName = moveCell.text().trim();

                        moveset.add(Integer.parseInt(level));
                        moveset.add(GlobalVariables.getInstance().getIDMovementFromName(normalizeName(moveName)));

                    }
                }

                Elements rowsMoveTutor = movesTables.get(1).select("tbody > tr");
                for (Element row : rowsMoveTutor) {
                    Elements cells = row.select("td");
                    if (cells.size() == 8) {
                        Element moveCell = cells.get(2);
                        String moveName = moveCell.text().trim();

                        movesTutor.add(GlobalVariables.getInstance().getIDMovementFromName(normalizeName(moveName)));

                    }
                }

                Elements rowsMoveBreeding = movesTables.get(2).select("tbody > tr");
                for (Element row : rowsMoveBreeding) {
                    Elements cells = row.select("td");
                    if (cells.size() == 7) {
                        Element moveCell = cells.get(1);
                        String moveName = moveCell.text().trim();

                        eggMoves.add(GlobalVariables.getInstance().getIDMovementFromName(normalizeName(moveName)));

                    }
                }

                ArrayList<Integer> typesIDs = new ArrayList<>();
                typesIDs.add(GlobalVariables.getInstance().getTypeFromName(normalizeName(type)).getId());

                ArrayList<Integer> abilitiesIDs = new ArrayList<>();
                abilitiesIDs.add(GlobalVariables.getInstance().getAbilityFromName(ability1).getId());

                int hdnAbilityID = GlobalVariables.getInstance().getAbilityFromName(ability2).getId();

                String internalName = normalizeNameNoLine(name);

                String description = "It releases a stench from its red antennae to repel enemies. It grows by molting repeatedly.";
                ArrayList<EggGroup> eggGroups2 = new ArrayList<>();
                eggGroups2.add(EggGroup.valueOf(eggGroup1));
                eggGroups2.add(EggGroup.valueOf(eggGroup2));

                lfj.addSpecie("json/species.json", 10, name, internalName, category, typesIDs, abilitiesIDs, hdnAbilityID, femaleRatio, catchRate,
                        eggGroups2, hatchTime, height, weight, baseExp, levelingRate, evs, baseFriendship, stats, moveset, movesTutor, eggMoves,
                        description);
            }

        }


    }

}
