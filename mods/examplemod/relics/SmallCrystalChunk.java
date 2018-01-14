package examplemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import modloader.ModLoader;

public class SmallCrystalChunk extends AbstractRelic {
    public static final String ID = "SmallCrystalChunk";
    public static final String IMG = "examplemod/relics/SmallCrystalChunk.png";
    public static final AbstractRelic.RelicTier TIER = AbstractRelic.RelicTier.SPECIAL;
    public static final AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.MAGICAL;
    
    public static final int BLOCK_AMT = 2;
    
    public SmallCrystalChunk() {
        super(ID, "", TIER, SFX);
        
        Texture t = new Texture(ModLoader.modRootPath + IMG);
        img = t;
        largeImg = t;
        outlineImg = t;
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BLOCK_AMT + DESCRIPTIONS[1];
    }
    
    @Override
    public void onPlayerEndTurn() {
        AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 2));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
    
    @Override
    public AbstractRelic makeCopy() {
        return new SmallCrystalChunk();
    }
}
    