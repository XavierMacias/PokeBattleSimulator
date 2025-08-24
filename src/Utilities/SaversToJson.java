package Utilities;

import Data.Ability;
import Data.Type;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class SaversToJson {
    public SaversToJson() {}

    public void saveType(ArrayList<Type> types, String filename) {
        File file = new File(filename);
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            gson.toJson(types, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAbility(ArrayList<Ability> types, String filename) {
        File file = new File(filename);
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            gson.toJson(types, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
