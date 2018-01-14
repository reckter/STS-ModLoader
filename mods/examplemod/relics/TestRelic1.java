package examplemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import modloader.ModLoader;

public class TestRelic1 extends AbstractRelic {
    public static final String ID = "TestRelic1";
    
    public static final AbstractRelic.RelicTier TIER = AbstractRelic.RelicTier.STARTER;
    public static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.MAGICAL;
    
    public static final String IMG = "examplemod/relics/TestRelic1.png";
    
    public TestRelic1() {
        super(ID, "", TIER, SOUND);
        
        Texture t = new Texture(ModLoader.modRootPath + IMG);
        img = t;
        largeImg = t;
        outlineImg = t;
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public void atPreBattle() {
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
    
    @Override
    public AbstractRelic makeCopy() {
        return new TestRelic1();
    }
}