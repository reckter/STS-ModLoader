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
    public ArrayList<Object> loadedCustomMonsters;
    
    public HashMap<String, CharacterMod> characterMods;
    
    public ModContainer() {
        this.customCardIds = new ArrayList<String>();
        this.loadedCustomCards = new ArrayList<Object>();
        
        this.customMonsters = new ArrayList<CustomMonster>();
        this.loadedCustomMonsters = new ArrayList<Object>();
        
        this.characterMods = new HashMap<String, CharacterMod>();     
    }
}