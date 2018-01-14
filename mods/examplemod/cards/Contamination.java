package examplemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import modloader.CustomCard;
import modloader.ModLoader;

import java.util.ArrayList;

public class Contamination extends CustomCard {
    public static final String ID = "Contamination";
    public static final String NAME = "Contamination";
    public static final String IMG = ModLoader.modRootPath + "examplemod/cards/contamination.png";
    public static final int COST = -2;
    public static final String DESC = "Infect NL Ethereal";
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.CURSE;
    public static final AbstractCard.CardColor COLOR = AbstractCard.CardColor.CURSE;
    public static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.CURSE;
    public static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.NONE;
    public static final int POOL = 1;
    
    public static final boolean ETHEREAL = true;
    public static final int POISON_AMT = 3;
    
    private AbstractPlayer player;
    
    public Contamination() {
       super(ID, NAME, IMG, COST, DESC, TYPE, COLOR, RARITY, TARGET, POOL); 
       isEthereal = ETHEREAL;
       
       player = AbstractDungeon.player;
    }
    
    private boolean transformRandomDiscard() {
        ArrayList<AbstractCard> discard = player.discardPile.group;
        if (discard.isEmpty()) {
            return false;
        } else {
            AbstractCard toRemove = player.discardPile.getRandomCard(true);
            discard.remove(toRemove);
            player.discardPile.addToBottom(new Contamination());
            return true;
        }
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasRelic("Blue Candle")) {
            useBlueCandle(p);
        }
    }
    
    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        dontTriggerOnUseCard = true;
        if (!transformRandomDiscard()) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction((AbstractCreature)player, (AbstractCreature)player, (AbstractPower)new PoisonPower(player, player, POISON_AMT), POISON_AMT, AbstractGameAction.AttackEffect.POISON));
        }
        
        AbstractDungeon.actionManager.addToTop(new ExhaustAllEtherealAction());
    }
    
    @Override
    public AbstractCard makeCopy() {
        return new Contamination();
    }
    
    @Override
    public void upgrade() {
    }
}