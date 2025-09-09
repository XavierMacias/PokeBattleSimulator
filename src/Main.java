import Data.Ability;
import Data.Type;
import Utilities.GetApi;
import Utilities.GlobalVariables;
import Utilities.LoadFromJson;
import Utilities.SaversToJson;

import java.io.File;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String TYPES = "json/types.json";
    private static final String ABILITIES = "json/abilities.json";
    private static final String MOVEMENTS = "json/movements.json";
    private static final String SPECIES = "json/species.json";

    public static void main(String[] args) throws IOException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Pokémon Battle Simulator\n");

        GetApi getApi = new GetApi();
        SaversToJson stj = new SaversToJson();
        LoadFromJson lfj = new LoadFromJson();

        File f = new File(TYPES);
        if(f.exists() && !f.isDirectory()) {
            GlobalVariables.getInstance().setPokemonTypes(lfj.loadTypes(TYPES));
        } else {
            try {
                getApi.getTypes();
                stj.saveType(GlobalVariables.getInstance().getPokemonTypes(), TYPES);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        File f2 = new File(ABILITIES);
        if(f2.exists() && !f2.isDirectory()) {
            GlobalVariables.getInstance().setPokemonAbilities(lfj.loadAbilities(ABILITIES));
        } else {
            try {
                getApi.getAbilities();
                stj.saveAbility(GlobalVariables.getInstance().getPokemonAbilities(), ABILITIES);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        File f3 = new File(MOVEMENTS);
        //GlobalVariables.getInstance().setPokemonMovements(lfj.loadMovements(MOVEMENTS));
        if(f3.exists() && !f3.isDirectory()) {
            GlobalVariables.getInstance().setPokemonMovements(lfj.loadMovements(MOVEMENTS));
        } else {
            try {
                getApi.getMoves();
                stj.saveMovement(GlobalVariables.getInstance().getPokemonMovements(), MOVEMENTS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        GlobalVariables.getInstance().setPokemonSpecies(lfj.loadSpecies(SPECIES));

        //getApi.getSpecies();

        for(int i=0;i<GlobalVariables.getInstance().getPokemonSpecies().size();i++) {
            System.out.println(GlobalVariables.getInstance().getPokemonSpecies().get(i).toString() + "\n");
        }


    }
}