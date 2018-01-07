package modloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModContainer {
    public String modName;
    public String modPackage;
    public String modDescription;
    public String modAuthor;
    public String modVersion;
    
    public List<String> customCardIds;
    public List<Object> customCards;
    
    public HashMap<String, CharacterMod> characterMods;
    
    public ModContainer() {
        this.customCardIds = new ArrayList<String>();
        this.customCards = new ArrayList<Object>();
        this.characterMods = new HashMap<String, CharacterMod>();
    }
}