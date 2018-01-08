package modloader;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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
    public ArrayList<Object> loadedCustomCards;
    
    public List<CustomMonster> customMonsters;
    public HashMap<String, Class> loadedCustomMonsterClasses;
    
    public HashMap<String, CharacterMod> characterMods;
    
    public ModContainer() {
        customCardIds = new ArrayList<String>();
        loadedCustomCards = new ArrayList<Object>();
        
        customMonsters = new ArrayList<CustomMonster>();
        loadedCustomMonsterClasses = new HashMap<String, Class>();
        
        characterMods = new HashMap<String, CharacterMod>();     
    }
}