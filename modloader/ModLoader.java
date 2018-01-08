package modloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.google.gson.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.mainMenu.CardLibraryScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class ModLoader {
    private static final Logger logger = LogManager.getLogger(ModLoader.class.getName());
      
    public static String modRootPath;
    private static ClassLoader loader;
    
    private static ArrayList<ModContainer> mods;
    
    private static HashMap<String, Integer> specialHealth = new HashMap<String, Integer>();
    private static String[] specialEnergy = {"Philosopher's Stone", "Velvet Choker", "Sozu", "Gremlin Horn", "Cursed Key", "Lantern"};   
    
    // Flags
    private static final boolean isDev = true;
    
    // initialize - Reinitializes with the existing root path
    public static void initialize() {
        initialize(modRootPath);
    }
    
    // initialize -
    public static void initialize(String path) {
        logger.info("========================= MOD LOADER INIT =========================");
        logger.info("");
        
        modRootPath = path;  
        hijackClassLoader();
        
        CardCrawlGame.modLoaderActive = true;
        CardCrawlGame.VERSION_NUM += " [MODLOADER ACTIVE]";
        Settings.isModded = true;
        
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
        
        // This is missing some
        specialHealth.put("Strawberry", 7);
        specialHealth.put("Pear", 10);
        specialHealth.put("Mango", 14);

        mods = loadMods();       
        generateCustomCards();
        loadCustomMonsters();
        
        logger.info("===================================================================");
    }
    
    // updateHook -
    public static void updateHook() {

    }
    
    // startGameHook -
    public static void startGameHook() {
        logger.info("====================== MOD LOADER START GAME ======================");
        logger.info("");
        
        modifyCharacter();
        
        logger.info("===================================================================");
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
    
    // exordiumWeakMonsterHook - add custom encounters to exordium weak pool
    public static void exordiumWeakMonsterHook(ArrayList<MonsterInfo> monsters) {
        for (ModContainer mod : mods) {
            for (CustomMonster m : mod.customMonsters) {
                if (m.group == CustomMonster.Group.WEAK && m.floor == CustomMonster.Floor.EXORDIUM) {
                    monsters.add(new MonsterInfo(m.id, m.weight));
                    logger.info("Added custom monster to exordium weak pool: " + m.id + "(" + m.weight + ")");
                }
            }
        }
    }
    
    // exordiumStrongMonsterHook - add custom encounters to exordium strong pool
    public static void exordiumStrongMonsterHook(ArrayList<MonsterInfo> monsters) {
        for (ModContainer mod : mods) {
            for (CustomMonster m : mod.customMonsters) {
                if (m.group == CustomMonster.Group.STRONG && m.floor == CustomMonster.Floor.EXORDIUM) {
                    monsters.add(new MonsterInfo(m.id, m.weight));
                    logger.info("Added custom monster to exordium strong pool: " + m.id);
                }
            }
        }
    }
    
    // exordiumEliteMonsterHook - add custom encounters to exordium elite pool
    public static void exordiumEliteMonsterHook(ArrayList<MonsterInfo> monsters) {
        for (ModContainer mod : mods) {
            for (CustomMonster m : mod.customMonsters) {
                if (m.group == CustomMonster.Group.ELITE && m.floor == CustomMonster.Floor.EXORDIUM) {
                    monsters.add(new MonsterInfo(m.id, m.weight));
                    logger.info("Added custom monster to exordium elite pool: " + m.id);
                }
            }
        }
    }
    
    // customMonsterEncounterHook - generate monster groups for custom monsters
    // TODO: Add support for groups rather than only single monsters
    public static MonsterGroup customEncounterHook(String key) {
        for (ModContainer mod : mods) {
            Class monsterClass = mod.loadedCustomMonsterClasses.get(key);
            if (monsterClass != null) {
                try {
                    MonsterGroup customMonsterGroup = new MonsterGroup((AbstractMonster)monsterClass.getDeclaredConstructor(float.class, float.class).newInstance(0.0f, 0.0f));
                    logger.info("Created custom monster group: " + key);
                    return customMonsterGroup;
                } catch (Exception e) {
                    logger.error("Exception in customEncounterHook", e);
                }
            }
        }
        
        // Apology slime
        logger.info("Could not find MonsterGroup: " + key + " (might be vanilla)");
        return null;
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
            
            // Initialize GSON
            GsonBuilder gsonBuilder = new GsonBuilder();           
            Gson gson = gsonBuilder.create();
            
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
            
            // Cleanup
            logger.info(modPackage + " loaded");
        }
        
        logger.info("All mods loaded");
        logger.info("");
        return mods;
    }
    
    // generateCustomCards -
    private static void generateCustomCards() {
        logger.info("Generating custom cards");

        for (ModContainer mod : mods) {
            for (String id : mod.customCardIds) {
                AbstractCard customCard = null; 
                
                try {
                    Object customCardObj = loader.loadClass(mod.modPackage + ".cards." + id).newInstance();
                    customCard = (AbstractCard) customCardObj;
                    mod.loadedCustomCards.add(customCard);
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
        logger.info("");
    }
    
    // loadCustomMonsters -
    private static void loadCustomMonsters() {
        logger.info("Loading custom monsters");
        
        for (ModContainer mod : mods) {
            for (CustomMonster m : mod.customMonsters) {
                try {
                    Class customMonsterClass = loader.loadClass(mod.modPackage + ".monsters." + m.id);
                    mod.loadedCustomMonsterClasses.put(m.id, customMonsterClass);
                } catch (Exception e) {
                    logger.error(mod.modName + ": Exception occured when loading monster " + m.id, e);
                }
                
                logger.info(mod.modName + ": " + m.id + " loaded");
            }
        }
        
        logger.info("All custom monsters loaded");
        logger.info("");
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
    
    // readFile - Helper function that returns a String representation of the contents of the file located at path
    // returns null if the file can not be accessed/found or is a folder
    private static String readFile(String path) {
        File f = new File(path);
        if (!f.exists() || f.isDirectory()) return null;
        
        String fileString = null;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();       
            while (line != null) {
                sb.append(line.trim());
                line = br.readLine();
            }
            
            fileString = sb.toString();
        } catch (Exception e) {
            logger.error("Exception while reading " + path + "", e);
        }
        
        return fileString;
    }
    
    // hijackClassLoader - Hijack the system ClassLoader for our use so we can cast properly
    // If anyone knows a way to make casting work without hijacking the system ClassLoader, let me know
    private static void hijackClassLoader() {
        try {
            File modRoot = new File(modRootPath);
            
            loader = ClassLoader.getSystemClassLoader();
            Class<URLClassLoader> loaderClass = URLClassLoader.class;
            Method addUrl = loaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
            addUrl.setAccessible(true);
            addUrl.invoke(loader, new Object[]{modRoot.toURI().toURL()});
        } catch (Exception e) {
            logger.error("Exception occured while hijacking system ClassLoader: ", e);
        }
    }
}