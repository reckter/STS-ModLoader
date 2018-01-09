package modloader;

import java.util.ArrayList;
import java.util.List;

public class CustomEncounter {
    public String id;
    public Floor floor;
    public Group group;
    public float weight;
    
    public List<String> monsters;
    
    public CustomEncounter() {
        monsters = new ArrayList<String>();
    }
    
    public enum Floor {
        EXORDIUM,
        CITY,
        BEYOND
    }

    public enum Group {
        WEAK,
        STRONG,
        ELITE,
        BOSS
    }
}

