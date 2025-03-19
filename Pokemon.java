import java.io.Serializable;
import java.util.List;

class Pokemon implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String type1;
    private String type2;
    private int total;
    private int hp;
    private int attack;
    private int defense;
    private int spAtk;
    private int spDef;
    private int speed;
    private int generation;
    private boolean legendary;
    private List<String> abilities;
    
    // Constructor
    public Pokemon(String name, String type1, String type2, int total, int hp, int attack, int defense,
                  int spAtk, int spDef, int speed, int generation, boolean legendary, List<String> abilities) {
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.total = total;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.spAtk = spAtk;
        this.spDef = spDef;
        this.speed = speed;
        this.generation = generation;
        this.legendary = legendary;
        this.abilities = abilities;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getType1() {
        return type1;
    }
    
    public String getType2() {
        return type2;
    }
    
    public int getTotal() {
        return total;
    }
    
    public boolean hasAbility(String ability) {
        return abilities.stream().anyMatch(a -> a.equalsIgnoreCase(ability));
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(name).append(" ===\n");
        sb.append("Tipo primario: ").append(type1).append("\n");
        sb.append("Tipo secundario: ").append(type2.isEmpty() ? "N/A" : type2).append("\n");
        sb.append("Estadísticas:\n");
        sb.append("- HP: ").append(hp).append("\n");
        sb.append("- Ataque: ").append(attack).append("\n");
        sb.append("- Defensa: ").append(defense).append("\n");
        sb.append("- Ataque Especial: ").append(spAtk).append("\n");
        sb.append("- Defensa Especial: ").append(spDef).append("\n");
        sb.append("- Velocidad: ").append(speed).append("\n");
        sb.append("Total: ").append(total).append("\n");
        sb.append("Generación: ").append(generation).append("\n");
        sb.append("Legendario: ").append(legendary ? "Sí" : "No").append("\n");
        sb.append("Habilidades: ").append(String.join(", ", abilities)).append("\n");
        return sb.toString();
    }
}