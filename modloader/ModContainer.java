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
    public ArrayList<AbstractCard> loadedCustomCards;
    
    public List<CustomMonster> customMonsters;
    public ArrayList<AbstractMonster> loadedCustomMonsters;
    
    public HashMap<String, CharacterMod> characterMods;
    
    public ModContainer() {
        this.customCardIds = new ArrayList<String>();
        this.loadedCustomCards = new ArrayList<AbstractCard>();
        
        this.customMonsters = new ArrayList<CustomMonster>();
        this.loadedCustomMonsters = new ArrayList<AbstractMonster>();
        
        this.characterMods = new HashMap<String, CharacterMod>();
        
    }
}