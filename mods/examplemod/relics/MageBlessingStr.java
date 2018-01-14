package examplemod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon; 
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import modloader.ModLoader;

public class MageBlessingStr extends AbstractRelic {
    public static final String ID = "MageBlessingStr";
    public static final String IMG = "examplemod/relics/MageBlessingStr.png";
    public static final AbstractRelic.RelicTier TIER = AbstractRelic.RelicTier.SPECIAL;
    public static final AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.MAGICAL;
    
    public static final int STR = 1;
    
    public MageBlessingStr() {
        super(ID, "", TIER, SFX);
        
        Texture t = new Texture(ModLoader.modRootPath + IMG);
        img = t;
        largeImg = t;
        outlineImg = t;
    }
    
    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + STR + this.DESCRIPTIONS[1];
    }
    
    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STR), STR));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
    
    @Override
    public AbstractRelic makeCopy() {
        return new MageBlessingStr();
    }
}