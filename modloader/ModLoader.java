package modloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.mainMenu.CardLibraryScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ModLoader {
    private static final Logger logger = LogManager.getLogger(ModLoader.class.getName());
      
    public static String modRootPath;
    private static ClassLoader loader;
    private static Gson gson;
    
    private static ArrayList<ModContainer> mods;
    
    private static HashMap<String, Integer> specialHealth = new HashMap<String, Integer>();
    private static String[] specialEnergy = {"Philosopher's Stone", "Velvet Choker", "Sozu", "Gremlin Horn", "Cursed Key", "Lantern"};   
    
    // Flags
    private static final boolean isDev = false;
    private static final boolean isTest = false;
    
    // initialize -
    public static void initialize(String path) {
        logger.info("========================= MOD LOADER INIT =========================");
        
        CardCrawlGame.VERSION_NUM += " [MODLOADER ACTIVE]";
        Settings.isModded = true; 
        
        // This is missing some
        specialHealth.put("Strawberry", 7);
        specialHealth.put("Pear", 10);
        specialHealth.put("Mango", 14);
        
        modRootPath = path;  
        buildClassLoader();
        initializeGson(); 
        
        mods = loadMods();  
          
        loadCustomEventStrings();
        loadCustomRelicStrings();
        loadCustomKeywords();
         
        loadCustomEvents();
        loadCustomMonsters();
        
        generateCustomCards();
        generateCustomRelics();
        
        if (isDev) {
            CardCrawlGame.splashScreen.isDone = true;
            Settings.ACTION_DUR_FAST = 0.1f;
            Settings.ACTION_DUR_MED = 0.2f;
            Settings.ACTION_DUR_LONG = 0.3f;
            Settings.ACTION_DUR_XLONG = 0.5f;
        } else {
            Settings.ACTION_DUR_FAST = 0.33f;
            Settings.ACTION_DUR_MED = 0.5f;
            Settings.ACTION_DUR_LONG = 1.0f;
            Settings.ACTION_DUR_XLONG = 1.5f;
        }
        
        logger.info("===================================================================");
        
    }
    
    // updateHook - Hotkeys
    public static void updateHook() {
        if (isTest) {
            // Check current event list
            if (Gdx.input.isKeyJustPressed(Keys.F5)) {
                if (AbstractDungeon.eventList != null) {
                    for (String event : AbstractDungeon.eventList) {
                        logger.info(event);
                    }
                }
            }  
            
            // Check current shrine list
            if (Gdx.input.isKeyJustPressed(Keys.F6)) {
                if (AbstractDungeon.shrineList != null) {
                    for (String shrine : AbstractDungeon.shrineList) {
                        logger.info(shrine);
                    }
                }
            }  
            
            // Generate 3 random potions - this works but the graphics mess up
            if (Gdx.input.isKeyJustPressed(Keys.F7)) {
                for (int i = 0; i < 3; i++) {
                    AbstractPotion p = PotionHelper.getRandomPotion();
                    p.moveInstantly(AbstractDungeon.player.potions[i].currentX, AbstractDungeon.player.potions[i].currentY);
                    AbstractDungeon.player.potions[i] = p;
                }
            } 
        }            
    }
    
    // startGameHook -
    public static void startGameHook() {
        modifyCharacter();
    }
    
    // charOptionHook - 
    public static void charOptionHook(CharSelectInfo info) {
        for (ModContainer mod : mods) {
            CharacterMod charMod = null;
            switch (info.color) {
                case IRONCLAD:
                    charMod = mod.characterMods.get("ironclad");
                    break;
                case THE_SILENT:
                    charMod = mod.characterMods.get("silent");
                    break;
                default:
                    break;
            }  
            
            if (charMod != null) {
                for (String relic : charMod.addRelics) {
                    info.relics.add(relic);
                    if (specialHealth.containsKey(relic)) {
                        String[] hpParts = info.hp.split("/");
                        int curHp = Integer.parseInt(hpParts[0]);
                        int maxHp = Integer.parseInt(hpParts[1]);
                        int addHp = specialHealth.get(relic);
                        
                        curHp += addHp;
                        maxHp += addHp;
                        info.hp = curHp + "/" + maxHp;
                    }
                }
                
                for (String relic : charMod.removeRelics) {
                    info.relics.remove(relic);
                }
            }
        }
    }

    // monsterPoolHook - Adds all CustomEncounters for the specified floor and group to the monsters List
    public static void monsterPoolHook(ArrayList<MonsterInfo> monsters, CustomEncounter.Floor floor, CustomEncounter.Group group) {
        for (ModContainer mod : mods) {
            for (CustomEncounter ce : mod.customEncounters) {
                if (ce.floor == floor && ce.group == group) {
                    monsters.add(new MonsterInfo(ce.id, ce.weight));
                    logger.info("Added CustomEncounter to " + floor + " " + group + " pool: " + ce.id + " (" + ce.weight + ")");
                }
            }
        }
    }
    
    // bossPoolHook -
    public static void bossPoolHook(ArrayList<String> bosses, CustomEncounter.Floor floor) {
        if (isTest) {
            bosses.clear();
        }
        
        for (ModContainer mod : mods) {
            for (CustomEncounter ce : mod.customEncounters) {
                if (ce.floor == floor && ce.group == CustomEncounter.Group.BOSS) {
                    bosses.add(ce.id);
                    logger.info("Added CustomEncounter to " + floor + " BOSS pool: " + ce.id);
                }
            }
        }
    }
    
    // customMonsterEncounterHook - generate a MonsterGroup for a CustomEncounter
    public static MonsterGroup customEncounterHook(String key) {
        for (ModContainer mod : mods) {
            CustomEncounter encounter = null;
            for (CustomEncounter ce : mod.customEncounters) {
                if (ce.id.equals(key)) {
                    encounter = ce;
                    break;
                }
            }
            
            if (encounter != null) {
                try {
                    if (encounter.monsters.size() == 1) {
                        Class monsterClass = Class.forName(mod.modPackage + ".monsters." + encounter.monsters.get(0));
                        AbstractMonster monster = (AbstractMonster) monsterClass.getDeclaredConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                        logger.info("Created custom monster group: " + key);
                        return new MonsterGroup(monster);
                    } else {
                        ArrayList<AbstractMonster> monsters = new ArrayList<AbstractMonster>();
                        float offx = 250.0f;
                        for (String mcName : encounter.monsters) {
                            Class monsterClass = Class.forName(mod.modPackage + ".monsters." + mcName); 
                            float hbx = (float) monsterClass.getDeclaredField("HBW").get(null);
                            offx -= (hbx*1.1f);
                            monsters.add((AbstractMonster) monsterClass.getDeclaredConstructor(float.class, float.class).newInstance(offx+(hbx*1.1f), 0.0f));
                        }
                        
                        AbstractMonster[] monstersArray = new AbstractMonster[monsters.size()];
                        monstersArray = monsters.toArray(monstersArray);
                        logger.info("Created custom monster group: " + key);
                        return new MonsterGroup(monstersArray);
                    }     
                } catch (Exception e) {
                    logger.error("Exception in customEncounterHook", e);
                } 
            }
        }
        
        logger.info("Did not find CustomEncounter: " + key);
        return null;
    }
    
    // customEventHook -
    public static AbstractEvent customEventHook(String key) {
        for (ModContainer mod : mods) {
            CustomEvent customEvent = null;
            for (CustomEvent ce : mod.customEvents) {
                if (ce.id.equals(key)) {
                    customEvent = ce;
                    break;
                }
            }
            
            if (customEvent != null) {
                try {
                    Class eventClass = Class.forName(mod.modPackage + ".events." + key);
                    AbstractEvent event = (AbstractEvent) eventClass.newInstance();
                    logger.info("Created custom event: " + key);
                    return event;
                } catch (Exception e) {
                    logger.error("Exception in customEventHook", e);
                }
            }
        }
        
        logger.info("Did not find CustomEvent: " + key);
        return null;
    }
    
    // eventListHook -
    public static void eventListHook(ArrayList<String> eventList, CustomEncounter.Floor floor) {
        if (isTest) {
            eventList.clear();
        }
        
        for (ModContainer mod : mods) {
            for (CustomEvent ce : mod.customEvents) {
                if (ce.eventType == CustomEvent.EventType.EVENT && ce.floor == floor) {
                    eventList.add(ce.id);
                    logger.info("Added CustomEvent to " + floor + " EVENT pool: " + ce.id);
                }
            }
        }
    }
    
    // shrineListHook -
    public static void shrineListHook(ArrayList<String> shrineList, CustomEncounter.Floor floor) {
        if (isTest) {
            shrineList.clear();
        }
        
        for (ModContainer mod : mods) {
            for (CustomEvent ce : mod.customEvents) {
                if (ce.eventType == CustomEvent.EventType.SHRINE && ce.floor == floor) {
                    shrineList.add(ce.id);
                    logger.info("Added CustomEvent to " + floor + " SHRINE pool: " + ce.id);
                }
            }
        }
    }
    
    // gameActionCreateHook -
    public static void gameActionCreateHook(AbstractGameAction action) {
        logger.info(action.getClass().getName());
    }
    
    // setFontSize -
    private static void setFontSize(float size) {
        try {
            Method prepFont = FontHelper.class.getDeclaredMethod("prepFont", new Class[]{float.class, boolean.class});
            prepFont.setAccessible(true);
            FontHelper.cardDescFont_N = (BitmapFont) prepFont.invoke(null, size, false);
            FontHelper.cardDescFont_L = (BitmapFont) prepFont.invoke(null, size, true);
        } catch (Exception e) {
            logger.error("Exception while setting font size", e);
        }
    }
    
    // initializeGson -
    private static void initializeGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }
    
    // loadMods -
    private static ArrayList<ModContainer> loadMods() {
        ArrayList<ModContainer> mods = new ArrayList<ModContainer>();   
        logger.info("Loading mods");
              
        File[] modRoots = new File(modRootPath).listFiles(File::isDirectory);
        for (int i = 0; i < modRoots.length; i++) {
            // Get package name, skip ModLoader
            String modPackage = modRoots[i].getName();
            String modPath = modRootPath + modPackage;
            
            if (modPackage.equals("modloader") || modPackage.charAt(0) == '.') continue;
            
            // Read and parse JSON
            String modJson = readFile(modPath + "/mod.json");
            if (modJson != null) {
                ModContainer mod = gson.fromJson(modJson, ModContainer.class);
                logger.info(modPackage + " loaded main");
                
                String ironcladJson = readFile(modPath + "/ironclad.json");
                if (ironcladJson != null) {
                    CharacterMod ironclad = gson.fromJson(ironcladJson, CharacterMod.class);
                    mod.characterMods.put("ironclad", ironclad);
                    logger.info(modPackage + " loaded Ironclad mods");
                }
                
                String silentJson = readFile(modPath + "/silent.json");
                if (silentJson != null) {
                    CharacterMod silent = gson.fromJson(silentJson, CharacterMod.class);
                    mod.characterMods.put("silent", silent);
                    logger.info(modPackage + " loaded Silent mods");
                }
                
                mods.add(mod);
            } else {
                logger.info(modPackage + " is missing mod.json, skipping");
            }
        }
        
        logger.info("All mods loaded");
        return mods;
    }
    
    // loadCustomRelicStrings -
    private static void loadCustomRelicStrings() {
        logger.info("Loading custom RelicStrings");
        
        Map<String, RelicStrings> customRelicStrings = new HashMap<String, RelicStrings>();
        for (ModContainer mod : mods) {
            String relicPath = modRootPath + mod.modPackage + "/localization/relics.json";
            Type relicType = new TypeToken<Map<String, RelicStrings>>(){}.getType();
            customRelicStrings.putAll(gson.fromJson(readFile(relicPath), relicType));
        }
        
        Map<String, RelicStrings> relicStrings = (Map<String, RelicStrings>) getPrivateStatic(LocalizedStrings.class, "relics");
        relicStrings.putAll(customRelicStrings);
        setPrivateStaticFinal(LocalizedStrings.class, "relics", relicStrings);
        
        logger.info("All custom RelicStrings loaded");
    }
    
    // loadCustomEventStrings -
    private static void loadCustomEventStrings() {
        logger.info("Loading custom EventStrings");
        
        Map<String, EventStrings> customEventStrings = new HashMap<String, EventStrings>();
        for (ModContainer mod : mods) {
            String eventPath = modRootPath + mod.modPackage + "/localization/events.json";
            Type eventType = new TypeToken<Map<String, EventStrings>>(){}.getType();
            customEventStrings.putAll(gson.fromJson(readFile(eventPath), eventType));
        }
        
        Map<String, EventStrings> eventStrings = (Map<String, EventStrings>) getPrivateStatic(LocalizedStrings.class, "events");
        eventStrings.putAll(customEventStrings);
        setPrivateStaticFinal(LocalizedStrings.class, "events", eventStrings);
        
        logger.info("All custom EventStrings loaded");
    }
    
    // loadCustomKeywords -
    private static void loadCustomKeywords() {
        logger.info("Loading custom keywords");

        TreeMap<String, String> customKeywords = new TreeMap<String, String>();
        for (ModContainer mod : mods) {
            String keywordPath = modRootPath + mod.modPackage + "/localization/keywords.json";
            Type keywordType = new TypeToken<Map<String, String>>(){}.getType();
            customKeywords.putAll(gson.fromJson(readFile(keywordPath), keywordType));
        }

        GameDictionary.keywords.putAll(customKeywords);
        
        logger.info("All custom keywords loaded");
    }
    
    // generateCustomCards -
    private static void generateCustomCards() {
        logger.info("Generating custom cards");

        for (ModContainer mod : mods) {
            for (String id : mod.customCardIds) {
                AbstractCard customCard = null; 
                
                try {
                    customCard = (AbstractCard) loader.loadClass(mod.modPackage + ".cards." + id).newInstance();
                    mod.customCards.add(customCard);
                } catch (Exception e) {
                    logger.error(mod.modName + ": Exception occured when generating card " + id, e);
                }
                
                if (customCard != null) {
                    CardLibrary.cards.remove(id); // I dont think this is enough to enable overriding existing cards - need to test
                    CardLibrary.add(customCard);
                    UnlockTracker.unlockCard(id);
                    logger.info(mod.modName + ": " + id + " generated");
                } else {
                    logger.error(mod.modName + ": " + id + " could not be generated, skipping");
                }
            }
        }
        
        logger.info("All custom cards generated");
    }
    
    // generateCustomRelics -
    private static void generateCustomRelics() {
        logger.info("Generating custom relics");
        
        for (ModContainer mod : mods) {
            for (String id : mod.customRelicIds) {
                AbstractRelic customRelic = null;
                
                try {
                    customRelic = (AbstractRelic) loader.loadClass(mod.modPackage + ".relics." + id).newInstance();
                    mod.customRelics.add(customRelic);
                } catch (Exception e) {
                    logger.error(mod.modName + ": Exception occured when generating relic " + id, e);
                }
                
                if (customRelic != null) {
                    RelicLibrary.add(customRelic);
                    logger.info(mod.modName + ": " + id + " generated");
                } else {
                    logger.error(mod.modName + ": " + id + " could not be generated, skipping");
                }
            }
        }
        
        logger.info("All custom relics generated");
    }
    
    // loadCustomEvents -
    private static void loadCustomEvents() {
        logger.info("Loading custom events");
        
        for (ModContainer mod : mods) {
            for (String id : mod.customEventIds) {
                try {
                    Class customEventClass = loader.loadClass(mod.modPackage + ".events." + id);
                } catch (Exception e) {
                    logger.error(mod.modName + ": Exception occured when loading event " + id, e);
                }
                
                logger.info(mod.modName + ": " + id + " loaded");
            }
        }
        
        logger.info("All custom events loaded");
    }
    
    // loadCustomMonsters -
    private static void loadCustomMonsters() {
        logger.info("Loading custom monsters");
        
        for (ModContainer mod : mods) {
            for (String id : mod.customMonsterIds) {
                try {
                    Class customMonsterClass = loader.loadClass(mod.modPackage + ".monsters." + id);
                    mod.customMonsters.put(id, customMonsterClass); 
                } catch (Exception e) {
                    logger.error(mod.modName + ": Exception occured when loading monster " + id, e);
                }
                
                logger.info(mod.modName + ": " + id + " loaded");
            }
        }
        
        logger.info("All custom monsters loaded");
    }
    
    // modifyCharacter -
    private static void modifyCharacter() {                    
        AbstractPlayer.PlayerClass playerClass = CardCrawlGame.dungeon.player.chosenClass;
        for (ModContainer mod : mods) {
            CharacterMod charMod = null;
            switch (playerClass) {
                case IRONCLAD:
                    charMod = mod.characterMods.get("ironclad");
                    break;
                case THE_SILENT:
                    charMod = mod.characterMods.get("silent");
                    break;
                default:
                    break;
            }
            
            if (charMod != null) {
                for (String card : charMod.addCards) {
                    AbstractCard c = CardLibrary.getCard(playerClass, card).makeCopy();
                    if (c.rarity != AbstractCard.CardRarity.BASIC) {
                        CardHelper.obtain(c.cardID, c.rarity, c.color);
                    }
                    
                    AbstractDungeon.player.masterDeck.addToTop(c);
                }
                
                for (String card : charMod.removeCards) {
                    AbstractDungeon.player.masterDeck.removeCard(card);
                }
                
                int index = AbstractDungeon.player.relics.size();
                for (String relic : charMod.addRelics) {
                    if (Arrays.asList(specialEnergy).contains(relic)) {
                        RelicLibrary.getRelic(relic).makeCopy(playerClass).instantObtain(AbstractDungeon.player, index, false);
                    } else if (specialHealth.containsKey(relic)) {
                        // HP Relics do not activate when added like this by default, manually increase HP
                        int hp = specialHealth.get(relic);
                        AbstractDungeon.player.maxHealth += hp;
                        AbstractDungeon.player.currentHealth += hp;
                        RelicLibrary.getRelic(relic).makeCopy().instantObtain(AbstractDungeon.player, index, false);
                    } else {
                        RelicLibrary.getRelic(relic).makeCopy().instantObtain(AbstractDungeon.player, index, false);
                    }
                    
                    CardCrawlGame.dungeon.relicsToRemoveOnStart.add(relic);
                    ++index;
                }
                
                for (String relic : charMod.removeRelics) {
                    AbstractDungeon.player.loseRelic(relic);
                    CardCrawlGame.dungeon.relicsToRemoveOnStart.remove(relic);
                }
            }
        }            
    }
    
    // readFile - Shorthand for gdx readString
    private static String readFile(String path) {
        return Gdx.files.absolute(path).readString(String.valueOf(StandardCharsets.UTF_8));
    }
    
    // buildClassLoader - Creates a child ClassLoader of the system ClassLoader to be used to bring in mod class files
    private static void buildClassLoader() {
        try {
            URL modRootURL = new File(modRootPath).toURI().toURL();
            loader = new URLClassLoader(new URL[]{modRootURL}, ClassLoader.getSystemClassLoader());
        } catch (Exception e) {
            logger.error("", e);
        }
    }
    
    // getPrivate - Hack to let us read private static fields when needed
    public static Object getPrivateStatic(Class objClass, String fieldName) {
        try {
            Field targetField = objClass.getDeclaredField(fieldName);
            targetField.setAccessible(true);
            return targetField.get(null);
        } catch (Exception e) {
            logger.error("Exception occured when getting private static field " + fieldName + " of " + objClass.getName(), e);
        }
        
        return null;
    }
    
    // setFinalStatic - Hack to let us modify (private) static (final) fields when needed
    public static void setPrivateStaticFinal(Class objClass, String fieldName, Object newValue) {
        try {
            Field targetField = objClass.getDeclaredField(fieldName);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            
            modifiersField.setAccessible(true);
            modifiersField.setInt(targetField, targetField.getModifiers() & ~Modifier.FINAL);

            targetField.setAccessible(true);
            targetField.set(null, newValue);
        } catch (Exception e) {
            logger.error("Exception occured when setting (private) static (final) field " + fieldName + " of " + objClass.getName(), e);
        }
    }
    
    // setPrivateInherited - Hack to let us modify private inherited fields when needed
    public static void setPrivateInherited(Object obj, Class objClass, String fieldName, Object newValue) {
        try {
            Field targetField = objClass.getSuperclass().getDeclaredField(fieldName);
            targetField.setAccessible(true);
            targetField.set(obj, newValue);
        } catch (Exception e) {
            logger.error("Exception occured when setting private inherited field " + fieldName + " of " + objClass.getName(), e);
        }
    }
}