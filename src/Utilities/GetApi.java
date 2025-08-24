package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Data.Ability;
import Data.Type;
import org.json.JSONArray;
import org.json.JSONObject;

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
}
