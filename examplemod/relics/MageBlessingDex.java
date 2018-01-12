package examplemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon; 
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modloader.ModLoader;

public class MageBlessingDex extends AbstractRelic {
    public static final String ID = "MageBlessingDex";
    public static final String IMG = "examplemod/relics/MageBlessingDex.png";
    public static final AbstractRelic.RelicTier TIER = AbstractRelic.RelicTier.SPECIAL;
    public static final AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.MAGICAL;
    
    public static final int DEX = 1;
    
    public MageBlessingDex() {
        super(ID, "", TIER, SFX);
        
        Texture t = new Texture(ModLoader.modRootPath + IMG);
        img = t;
        largeImg = t;
        outlineImg = t;
    }
    
    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + DEX + this.DESCRIPTIONS[1];
    }
    
    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, DEX), DEX));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
    
    @Override
    public AbstractRelic makeCopy() {
        return new MageBlessingDex();
    }
}