package examplemod.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionPlaceholder;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import modloader.ModLoader;

public class InjuredMage extends AbstractImageEvent {
    public static final String ID = "InjuredMage";
    
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS; 
    public static final String IMG = ModLoader.modRootPath + "examplemod/events/InjuredMage.png";
    
    private static CUR_SCREEN screen = CUR_SCREEN.INTRO;
    private static AbstractPotion[] potions;
    
    public InjuredMage() {
        super(NAME, DESCRIPTIONS[0], IMG);
        potions = AbstractDungeon.player.potions;
        
        String opt1 = potions[0].ID.equals("Potion Slot") ? OPTIONS[0] : OPTIONS[3];
        String opt2 = potions[1].ID.equals("Potion Slot") ? OPTIONS[1] : OPTIONS[3];
        String opt3 = potions[2].ID.equals("Potion Slot") ? OPTIONS[2] : OPTIONS[3];
        
        GenericEventDialog.setDialogOption(opt1);
        GenericEventDialog.setDialogOption(opt2);
        GenericEventDialog.setDialogOption(opt3);
        GenericEventDialog.setDialogOption(OPTIONS[18]);
    }
    
    @Override
    public void update() {
        super.update();
        if (!(AbstractDungeon.isScreenUp || AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() || AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeStatEquivalentCopy();
            c.inBottleFlame = false;
            c.inBottleLightning = false;
            c.inBottleTornado = false;
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0f, (float)Settings.HEIGHT / 2.0f));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }
    
    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screen) {
            case INTRO: {
                introButtonEffect(buttonPressed);
                break;
            }
            case DEAD: {
                deadButtonEffect(buttonPressed);
                break;
            }
            case BLOCK: {
                blockButtonEffect(buttonPressed);
                break;
            }
            case BLOCK_ABSORB: {
                blockAbsorbButtonEffect(buttonPressed);
                break;
            }
            case DEXTERITY: {
                dexterityButtonEffect(buttonPressed);
                break;
            }
            case DEXTERITY_ABSORB: {
                dexterityAbsorbButtonEffect(buttonPressed);
                break;
            }
            case ELIXER: {
                elixerButtonEffect(buttonPressed);
                break;
            }
            case ELIXER_POWER: {
                elixerPowerButtonEffect(buttonPressed);
                break;
            }
            case ELIXER_PROTECTION: {
                elixerProtectionButtonEffect(buttonPressed);
                break;
            }
            case ELIXER_VIGOR: {
                elixerVigorButtonEffect(buttonPressed);
                break;
            }
            case ENERGY: {
                energyButtonEffect(buttonPressed);
                break;
            }
            case EXPLOSIVE: {
                explosiveButtonEffect(buttonPressed);
                break;
            }
            case FIRE: {
                fireButtonEffect(buttonPressed);
                break;
            }
            case POISON: {
                poisonButtonEffect(buttonPressed);
                break;
            }
            case REGEN: {
                regenButtonEffect(buttonPressed);
                break;
            }
            case STRENGTH: {
                strengthButtonEffect(buttonPressed);
                break;
            }
            case STRENGTH_ABSORB: {
                strengthAbsorbButtonEffect(buttonPressed);
                break;
            }
            case SWIFT: {
                swiftButtonEffect(buttonPressed);
                break;
            }
            case SWIFT_ABSORB: {
                swiftAbsorbButtonEffect(buttonPressed);
                break;
            }
            case WEAK: {
                weakButtonEffect(buttonPressed);
                break;
            }
            case LEFT: {
                leftButtonEffect(buttonPressed);
                break;
            }            
        }
    }
    
    private  void givePotion(int pos) {
        String toGive = potions[pos].ID;
        AbstractDungeon.player.potions[pos] = new PotionPlaceholder(pos);
        
        int roll = AbstractDungeon.eventRng.random(0, 99);
        if (roll < 10) {
            screen = CUR_SCREEN.DEAD;
            GenericEventDialog.updateBodyText(DESCRIPTIONS[1]);
            GenericEventDialog.removeDialogOption(3);
            GenericEventDialog.removeDialogOption(2);
            GenericEventDialog.removeDialogOption(1);
            GenericEventDialog.updateDialogOption(0, OPTIONS[19]);
            return;
        }
        
        switch (toGive) {
            case "Block Potion": {
                screen = CUR_SCREEN.BLOCK;
                GenericEventDialog.updateBodyText(DESCRIPTIONS[2]);
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.updateDialogOption(0, OPTIONS[4]);
                GenericEventDialog.updateDialogOption(1, OPTIONS[19]);
                break;
            }
            case "Dexterity Potion": {
                screen = CUR_SCREEN.DEXTERITY;
                GenericEventDialog.updateBodyText(DESCRIPTIONS[4]);
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.updateDialogOption(0, OPTIONS[5]);
                GenericEventDialog.updateDialogOption(1, OPTIONS[19]);
                break;
            }
            case "Elixer": {
                screen = CUR_SCREEN.ELIXER;
                GenericEventDialog.updateBodyText(DESCRIPTIONS[6]);
                GenericEventDialog.updateDialogOption(0, OPTIONS[6]);
                GenericEventDialog.updateDialogOption(1, OPTIONS[7]);
                GenericEventDialog.updateDialogOption(2, OPTIONS[8]);
                GenericEventDialog.updateDialogOption(3, OPTIONS[19]);
                break;
            }
            case "Energy Potion": {
                screen = CUR_SCREEN.ENERGY;
                GenericEventDialog.updateBodyText(DESCRIPTIONS[10]);
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.removeDialogOption(1);
                GenericEventDialog.updateDialogOption(0, OPTIONS[9]);
                break;
            }
            case "Explosive Potion": {
                screen = CUR_SCREEN.EXPLOSIVE;
                GenericEventDialog.updateBodyText(DESCRIPTIONS[11]);
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.removeDialogOption(1);
                GenericEventDialog.updateDialogOption(0, OPTIONS[10] + (AbstractDungeon.player.currentHealth / 4) + OPTIONS[11]);
                break;
            }
            case "Fire Potion": {
                screen = CUR_SCREEN.FIRE;
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.removeDialogOption(1);
                GenericEventDialog.updateDialogOption(0, OPTIONS[12]);
                GenericEventDialog.updateBodyText(DESCRIPTIONS[12]);
                break;
            }
            case "Poison Potion": {
                screen = CUR_SCREEN.POISON;
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.removeDialogOption(1);
                GenericEventDialog.updateDialogOption(0, OPTIONS[13]);
                GenericEventDialog.updateBodyText(DESCRIPTIONS[13]);
                break;
            }
            case "Regen Potion": {
                screen = CUR_SCREEN.REGEN;
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.removeDialogOption(1);
                GenericEventDialog.updateDialogOption(0, OPTIONS[14]);
                GenericEventDialog.updateBodyText(DESCRIPTIONS[14]);
                break;
            }
            case "Strength Potion": {
                screen = CUR_SCREEN.STRENGTH;
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.updateDialogOption(0, OPTIONS[15]);
                GenericEventDialog.updateDialogOption(1, OPTIONS[19]);
                GenericEventDialog.updateBodyText(DESCRIPTIONS[15]);
                break;
            }
            case "Swift Potion": {
                screen = CUR_SCREEN.SWIFT;
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.updateDialogOption(0, OPTIONS[16]);
                GenericEventDialog.updateDialogOption(1, OPTIONS[19]);
                GenericEventDialog.updateBodyText(DESCRIPTIONS[16]);
                break;
            }
            case "Weak Potion": {
                screen = CUR_SCREEN.WEAK;
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.removeDialogOption(1);
                GenericEventDialog.updateDialogOption(0, OPTIONS[17]);
                GenericEventDialog.updateBodyText(DESCRIPTIONS[17]);
                break;
            }
            default: {
                screen = CUR_SCREEN.DEAD;
                GenericEventDialog.updateBodyText(DESCRIPTIONS[1]);
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.removeDialogOption(1);
                GenericEventDialog.updateDialogOption(0, OPTIONS[19]);
                break;
            }
        }
    }
    
    private  void goToLeft() {
        screen = CUR_SCREEN.LEFT;
        GenericEventDialog.updateBodyText(DESCRIPTIONS[18]);
        GenericEventDialog.removeDialogOption(3);
        GenericEventDialog.removeDialogOption(2);
        GenericEventDialog.removeDialogOption(1);
        GenericEventDialog.updateDialogOption(0, OPTIONS[19]);
    }
    
    private  void introButtonEffect(int buttonPressed) {
        if (buttonPressed == 3) {
            goToLeft();
            return;
        }
        
        if (potions[buttonPressed].ID == "Potion Slot") {
            return;
        }
        
        givePotion(buttonPressed);
    }
    
    private  void deadButtonEffect(int buttonPressed) {
        goToLeft();
    }
    
    private  void blockButtonEffect(int buttonPressed) {
        if (buttonPressed == 1) {
            goToLeft();
            return;
        }
        
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, RelicLibrary.getRelic("SmallCrystalShard").makeCopy());
        
        screen = CUR_SCREEN.BLOCK_ABSORB;
        GenericEventDialog.updateBodyText(DESCRIPTIONS[3]);
        GenericEventDialog.removeDialogOption(3);
        GenericEventDialog.removeDialogOption(2);
        GenericEventDialog.removeDialogOption(1);
        GenericEventDialog.updateDialogOption(0, OPTIONS[19]);
    }
    
    private  void blockAbsorbButtonEffect(int buttonPressed) {
        goToLeft();
    }
    
    private  void dexterityButtonEffect(int buttonPressed) {
        if (buttonPressed == 1) {
            goToLeft();
            return;
        }
        
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, RelicLibrary.getRelic("MageBlessingDex").makeCopy());
        
        screen = CUR_SCREEN.DEXTERITY_ABSORB;
        GenericEventDialog.updateBodyText(DESCRIPTIONS[5]);
        GenericEventDialog.removeDialogOption(3);
        GenericEventDialog.removeDialogOption(2);
        GenericEventDialog.removeDialogOption(1);
        GenericEventDialog.updateDialogOption(0, OPTIONS[19]);
    }
    
    private  void dexterityAbsorbButtonEffect(int buttonPressed) {
        goToLeft();
    }
    
    private  void elixerButtonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0: {
               AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, RelicLibrary.getRelic("MageBlessingStr").makeCopy());
        
                screen = CUR_SCREEN.ELIXER_POWER;
                GenericEventDialog.updateBodyText(DESCRIPTIONS[7]);
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.removeDialogOption(1);
                GenericEventDialog.updateDialogOption(0, OPTIONS[19]); 
            }
            case 1: {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, RelicLibrary.getRelic("MageBlessingDex").makeCopy());
        
                screen = CUR_SCREEN.ELIXER_PROTECTION;
                GenericEventDialog.updateBodyText(DESCRIPTIONS[8]);
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.removeDialogOption(1);
                GenericEventDialog.updateDialogOption(0, OPTIONS[19]); 
            }
            case 2: {
                AbstractDungeon.player.maxHealth += 7;
                AbstractDungeon.player.currentHealth += 7;
                
                screen = CUR_SCREEN.ELIXER_VIGOR;
                GenericEventDialog.updateBodyText(DESCRIPTIONS[9]);
                GenericEventDialog.removeDialogOption(3);
                GenericEventDialog.removeDialogOption(2);
                GenericEventDialog.removeDialogOption(1);
                GenericEventDialog.updateDialogOption(0, OPTIONS[19]); 
            }
            case 3: {
                goToLeft();
                break;
            }
        }
    }
    
    private  void elixerPowerButtonEffect(int buttonPressed) {
        goToLeft();
    }
    
    private  void elixerProtectionButtonEffect(int buttonPressed) {
        goToLeft();
    }
    
    private  void elixerVigorButtonEffect(int buttonPressed) {
        goToLeft();
    }
    
    private  void energyButtonEffect(int buttonPressed) {
        // Implement chest luck
        
        goToLeft();
    }
    
    private  void explosiveButtonEffect(int buttonPressed) {
        int hpLoss = AbstractDungeon.player.currentHealth / 4;
        AbstractDungeon.player.currentHealth -= hpLoss;
        
        goToLeft();
    }
    
    private  void fireButtonEffect(int buttonPressed) {
        AbstractDungeon.player.masterDeck.addToTop(CardLibrary.getCopy("Guilt"));
        
        goToLeft();
    }
    
    private  void poisonButtonEffect(int buttonPressed) {
        AbstractDungeon.player.masterDeck.addToTop(CardLibrary.getCopy("Contamination"));
        
        goToLeft();
    }
    
    private  void regenButtonEffect(int buttonPressed) {
        AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
        
        goToLeft();
    }
    
    private  void strengthButtonEffect(int buttonPressed) {
        if (buttonPressed == 1) {
            goToLeft();
            return;
        }
        
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, RelicLibrary.getRelic("MageBlessingStr").makeCopy());
        
        screen = CUR_SCREEN.STRENGTH_ABSORB;
        GenericEventDialog.updateBodyText(DESCRIPTIONS[19]);
        GenericEventDialog.removeDialogOption(3);
        GenericEventDialog.removeDialogOption(2);
        GenericEventDialog.removeDialogOption(1);
        GenericEventDialog.updateDialogOption(1, OPTIONS[19]);
    }
    
    private  void strengthAbsorbButtonEffect(int buttonPressed) {
        goToLeft();
    }
    
    private  void swiftButtonEffect(int buttonPressed) {
        if (buttonPressed == 0) {
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, "Select a card to duplicate", false, false, false, false);
        }
        
        goToLeft();
    }
    
    private  void swiftAbsorbButtonEffect(int buttonPressed) {
        goToLeft();
    }
    
    private  void weakButtonEffect(int buttonPressed) {
        AbstractDungeon.player.currentHealth -= 7;
        AbstractDungeon.player.maxHealth -= 7;
        
        goToLeft();
    }
    
    private  void leftButtonEffect(int buttonPressed) {
        openMap();
    }
    
    private static enum CUR_SCREEN {
        INTRO,
        DEAD,
        BLOCK,
        BLOCK_ABSORB,
        DEXTERITY,
        DEXTERITY_ABSORB,
        ELIXER,
        ELIXER_POWER,
        ELIXER_PROTECTION,
        ELIXER_VIGOR,
        ENERGY,
        EXPLOSIVE,
        FIRE,
        POISON,
        REGEN,
        STRENGTH,
        STRENGTH_ABSORB,
        SWIFT,
        SWIFT_ABSORB,
        WEAK,
        LEFT
    }
}