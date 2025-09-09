package Data;

public class Ability {
    private int id;
    private String name;
    private String displayName;
    private String description;

    public Ability(int id, String name, String displayName, String description) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }

    public void printAbility() {
        System.out.print("ID:" + this.id + "\n NOMBRE INTERNO: " + this.name + "\n NOMBRE: " + this.displayName + "\n DESCRIPCIÓN: " + this.description);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "Ability{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
