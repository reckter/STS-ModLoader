package modloader;

public class CustomMonster {
    public String id;
    public Floor floor;
    public Group group;
    public float weight;
    
    public enum Floor {
        EXORDIUM,
        CITY,
        BEYOND
    }

    public enum Group {
        WEAK,
        STRONG,
        ELITE
    }
}

