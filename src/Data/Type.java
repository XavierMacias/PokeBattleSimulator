package Data;

import java.util.ArrayList;

public class Type {
    private int id;
    private String name;
    private String displayName;
    private ArrayList<Integer> debilities;
    private ArrayList<Integer> resistances;
    private ArrayList<Integer> inmunities;

    public Type(int id, String name, String displayName, ArrayList<Integer> debilities, ArrayList<Integer> resistances, ArrayList<Integer> inmunities) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.debilities = debilities;
        this.resistances = resistances;
        this.inmunities = inmunities;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getDisplayName() { return displayName; }

    public ArrayList<Integer> getDebilities() { return debilities; }

    public ArrayList<Integer> getResistances() { return resistances; }

    public ArrayList<Integer> getInmunities() { return inmunities; }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDebilities(ArrayList<Integer> debilities) {
        this.debilities = debilities;
    }

    public void setResistances(ArrayList<Integer> resistances) {
        this.resistances = resistances;
    }

    public void setInmunities(ArrayList<Integer> inmunities) {
        this.inmunities = inmunities;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", debilities=" + debilities +
                ", resistances=" + resistances +
                ", inmunities=" + inmunities +
                '}';
    }
}
