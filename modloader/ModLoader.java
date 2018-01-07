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
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class ModLoader {
    private static final Logger logger = LogManager.getLogger(ModLoader.class.getName());
      
    public static String modRootPath;
    private static ClassLoader loader;
    private static boolean isFirstLoad = true;
    
    private static ArrayList<ModContainer> mods;
    
    private static HashMap<String, Integer> specialHealth = new HashMap<String, Integer>();
    private static String[] specialEnergy = {"Philosopher's Stone", "Velvet Choker", "Sozu", "Gremlin Horn", "Cursed Key", "Lantern"};   
    
    // Flags
    private static final boolean isDev = false;
    
    // initialize - Reinitializes with the existing root path
    public static void initialize() {
        initialize(modRootPath);
    }
    
    // initialize -
    public static void initialize(String path) {
        logger.info("========================= MOD LOADER INIT =========================");
        logger.info("");
        
        // First time setup
        if (isFirstLoad) {
            modRootPath = path;  
            constructClassLoader();
            
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
            
            specialHealth.put("Strawberry", 7);
            specialHealth.put("Pear", 10);
            specialHealth.put("Mango", 14);
        }
        
        mods = loadMods();       
        generateCustomCards();
        generateCustomMonsters();
        
        isFirstLoad = false;
        logger.info("===================================================================");
    }
    
    // updateHook -
    public static void updateHook() {
        if (Gdx.input.isKeyJustPressed(Keys.F5)) {
            ModLoader.initialize(modRootPath);
        }
        
        if (Gdx.input.isKeyJustPressed(Keys.F6)) {
            logger.info("isModded: " + Settings.isModded);
        }
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
    
    // exordiumWeakMonsterHook -
    public static void exordiumWeakMonsterHook(ArrayList<MonsterInfo> monsters) {
        
    }
    
    // exordiumStrongMonsterHook -
    public static void exordiumStrongMonsterHook(ArrayList<MonsterInfo> monsters) {
        
    }
    
    // exordiumEliteMonsterHook -
    public static void exordiumEliteMonsterHook(ArrayList<MonsterInfo> monsters) {
        
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
            //if (!modPackage.equals("***")) continue;
            
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
                    CardLibrary.cards.remove(id);
                    CardLibrary.add(customCard);
                    UnlockTracker.unlockCard(id);
                    logger.info(mod.modName + ": " + id + " generated");
                } else {
                    logger.error(mod.modName + ": " + id + " could not be generated, skipping");
                }
            }
        }
        
        // Cleanup
        if (!isFirstLoad) {
            CardCrawlGame.mainMenuScreen.cardLibraryScreen.initialize();
            CardCrawlGame.mainMenuScreen.cardLibraryScreen.sortOnOpen();
        }
        
        logger.info("All custom cards generated");
        logger.info("");
    }
    
    private static void generateCustomMonsters() {
        logger.info("Generating custom monsters");
        
        for (ModContainer mod : mods) {
            for (CustomMonster m : mod.customMonsters) {
                AbstractMonster customMonster = null;
                
                try {
                    Object customMonsterObj = loader.loadClass(mod.modPackage + ".monsters." + m.id).newInstance();
                    customMonster = (AbstractMonster) customMonsterObj;
                    mod.loadedCustomMonsters.add(customMonster);
                } catch (Exception e) {
                    logger.error(mod.modName + ": Exception occured when generating monster " + m.id, e);
                }
                
                if (customMonster != null) {
                    logger.info(mod.modName + ": " + m.id + " generated");
                } else {
                    logger.error(mod.modName + ": " + m.id + " could not be generated, skipping");
                }
            }
        }
        
        logger.info("All custom monsters generated");
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
        // Check if the file exists
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
    
    // constructClassLoader - Helper function that builds a ClassLoader
    private static void constructClassLoader() {
        loader = null;     
        try {
            File modRoot = new File(modRootPath);
            URL[] urls = new URL[] {modRoot.toURI().toURL()};
            loader = new URLClassLoader(urls);
        } catch (Exception e) {
            logger.error("Exception occured when constructing ClassLoader: ", e);
        }
    }
}