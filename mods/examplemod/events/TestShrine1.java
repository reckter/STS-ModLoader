package examplemod.events;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;

import modloader.ModLoader;

public class TestShrine1 extends AbstractImageEvent {
    public static final String ID = "TestShrine1";
    
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    
    private static final String DIALOG_1 = DESCRIPTIONS[0];
    private static final String DIALOG_2 = DESCRIPTIONS[1];
    private static final String DIALOG_3 = DESCRIPTIONS[2];
    private static final String LEFT = DESCRIPTIONS[3];
    
    private static final String IMG = ModLoader.modRootPath + "examplemod/events/testshrine1.png";
    private CUR_SCREEN screen = CUR_SCREEN.ONE;
    
    public TestShrine1() {
        super(NAME, DIALOG_1, IMG);

        GenericEventDialog.setDialogOption(OPTIONS[1]);
        GenericEventDialog.setDialogOption(OPTIONS[2]);
        GenericEventDialog.setDialogOption(OPTIONS[3]);
    }
    
    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screen) {
            case ONE: {
                switch (buttonPressed) {
                    case 0: {
                        screen = CUR_SCREEN.TWO;
                        GenericEventDialog.updateBodyText(DIALOG_2);
                        GenericEventDialog.updateDialogOption(0, OPTIONS[0]);
                        GenericEventDialog.updateDialogOption(1, OPTIONS[2]);
                        break;
                    }
                    case 1: {
                        screen = CUR_SCREEN.THREE;
                        GenericEventDialog.updateBodyText(DIALOG_3);
                        GenericEventDialog.updateDialogOption(0, OPTIONS[0]);
                        GenericEventDialog.updateDialogOption(1, OPTIONS[1]);
                        break;
                    }
                    default: {
                        screen = CUR_SCREEN.LEFT;
                        GenericEventDialog.updateBodyText(LEFT);
                        GenericEventDialog.removeDialogOption(2);
                        GenericEventDialog.removeDialogOption(1);
                        GenericEventDialog.updateDialogOption(0, OPTIONS[3]);
                        openMap();
                        break;                        
                    }
                }
                break;
            }
            case TWO: {
                switch (buttonPressed) {
                    case 0: {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, RelicLibrary.getRelic("TestRelic1").makeCopy());
                        
                        screen = CUR_SCREEN.ONE;
                        GenericEventDialog.updateBodyText(DIALOG_1);
                        GenericEventDialog.updateDialogOption(0, OPTIONS[1]);
                        GenericEventDialog.updateDialogOption(1, OPTIONS[2]);
                        break;
                    }
                    case 1: {
                        screen = CUR_SCREEN.THREE;
                        GenericEventDialog.updateBodyText(DIALOG_3);
                        GenericEventDialog.updateDialogOption(0, OPTIONS[0]);
                        GenericEventDialog.updateDialogOption(1, OPTIONS[1]);
                        break;
                    }
                    default: {
                        screen = CUR_SCREEN.LEFT;
                        GenericEventDialog.updateBodyText(LEFT);
                        GenericEventDialog.removeDialogOption(2);
                        GenericEventDialog.removeDialogOption(1);
                        GenericEventDialog.updateDialogOption(0, OPTIONS[3]);
                        openMap();
                        break;                        
                    }
                }
                break;
            }
            case THREE: {
                switch (buttonPressed) {
                    case 0: {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, RelicLibrary.getRelic("TestRelic1").makeCopy());
                        
                        screen = CUR_SCREEN.ONE;
                        GenericEventDialog.updateBodyText(DIALOG_1);
                        GenericEventDialog.updateDialogOption(0, OPTIONS[1]);
                        GenericEventDialog.updateDialogOption(1, OPTIONS[2]);
                        break;
                    }
                    case 1: {
                        screen = CUR_SCREEN.TWO;
                        GenericEventDialog.updateBodyText(DIALOG_2);
                        GenericEventDialog.updateDialogOption(0, OPTIONS[0]);
                        GenericEventDialog.updateDialogOption(1, OPTIONS[2]);
                        break;
                    }
                    default: {
                        screen = CUR_SCREEN.LEFT;
                        GenericEventDialog.updateBodyText(LEFT);
                        GenericEventDialog.removeDialogOption(2);
                        GenericEventDialog.removeDialogOption(1);
                        GenericEventDialog.updateDialogOption(0, OPTIONS[3]);
                        openMap();
                        break;                        
                    }
                }
                break;
            }
            default: {
                openMap();
                break;
            }
        }
    }
    
    private static enum CUR_SCREEN {
        ONE,
        TWO,
        THREE,
        LEFT
    }
}