package modloader;

import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModContainer {
    // Deserialized by GSON
    public String modName;
    public String modPackage;
    public String modDescription;
    public String modAuthor;
    public String modVersion;
    
    public List<String> customCardIds;
    public List<String> customRelicIds;
    public List<String> customMonsterIds;
    public List<CustomEncounter> customEncounters;
    
    // Manually populated
    public ArrayList<Object> customCards; // Things break if this is an ArrayList<AbstractCard> - no idea why
    public ArrayList<AbstractRelic> customRelics;
    public HashMap<String, Class> customMonsters;
    public HashMap<String, CharacterMod> characterMods;
    
    public ModContainer() {
        customCardIds = new ArrayList<String>();
        customRelicIds = new ArrayList<String>();
        customMonsterIds = new ArrayList<String>();
        customEncounters = new ArrayList<CustomEncounter>();
        
        customCards = new ArrayList<Object>();
        customRelics = new ArrayList<AbstractRelic>();
        customMonsters = new HashMap<String, Class>();
        characterMods = new HashMap<String, CharacterMod>();     
    }
}