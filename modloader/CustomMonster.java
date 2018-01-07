package modloader;

public class CustomMonster {
    public String id;
    public Floor floor;
    public Group group;
    public float weight;
    
    private enum Floor {
        EXORDIUM,
        CITY,
        BEYOND
    }

    private enum Group {
        WEAK,
        STRONG,
        ELITE
    }
}

