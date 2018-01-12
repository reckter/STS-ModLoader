package examplemod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import modloader.CustomCard;
import modloader.ModLoader;

public class Guilt extends CustomCard {
    public static final String ID = "Guilt";
    public static final String NAME = "Guilt";
    public static final String IMG = ModLoader.modRootPath + "examplemod/cards/guilt.png";
    public static final int COST = -2;
    public static final String DESC = "Place 2 Guilt in your discard pile. NL Ethereal";
    public static final AbstractCard.CardType TYPE = AbstractCard.CardType.CURSE;
    public static final AbstractCard.CardColor COLOR = AbstractCard.CardColor.CURSE;
    public static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.CURSE;
    public static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.NONE;
    public static final int POOL = 1;
    
    public static final int GUILT_AMT = 2;
    public static final boolean ETHEREAL = true;
    
    public Guilt() {
        super(ID, NAME, IMG, COST, DESC, TYPE, COLOR, RARITY, TARGET, POOL);
        
        isEthereal = ETHEREAL;      
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
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(makeStatEquivalentCopy(), GUILT_AMT));
        AbstractDungeon.actionManager.addToTop(new ExhaustAllEtherealAction());
    }
    
    @Override
    public void triggerOnEndOfPlayerTurn() {
        
    }
    
    @Override
    public AbstractCard makeCopy() {
        return new Guilt();
    }
    
    @Override
    public void upgrade() {
    }
}