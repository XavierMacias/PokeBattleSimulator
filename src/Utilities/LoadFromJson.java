package Utilities;

import Data.Ability;
import Data.Type;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        ArrayList<Ability> types = new ArrayList<>();
        try {
            JSONArray jsonArray = parseJSON(filename);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject abilityJSON = jsonArray.getJSONObject(i);

                Ability ability = new Ability(abilityJSON.getInt("id"), abilityJSON.getString("name"), abilityJSON.getString("displayName"),
                        abilityJSON.getString("description"));
                types.add(ability);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return types;
    }
}
