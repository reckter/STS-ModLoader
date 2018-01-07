package examplemod.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import modloader.ModLoader;

public class TestSquare extends AbstractMonster {
    public static final String ID = "TestSquare";
    public static final String NAME = "Test Square";
    
    public static final int HP_MAX = 24;
    public static final int HP_MIN = 23;
    
    public static final int HIT_DMG = 9;
    public static final int GUARD_DMG = 4;
    public static final int GUARD_BLK = 10;
    
    public static final float HBX = 0.0f;
    public static final float HBY = 0.0f;
    public static final float HBW = 200.0f;
    public static final float HBH = 200.0f; 
    public static final String IMG = "examplemod/testsquare.png";

    private static final byte HIT = 1;
    private static final byte GUARD = 2;
    
    public TestSquare(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(HP_MIN, HP_MAX), HBX, HBY, HBW, HBH, null, x, y);
        damage.add(new DamageInfo(this, HIT_DMG));
        damage.add(new DamageInfo(this, GUARD_DMG));
        
        img = new Texture(ModLoader.modRootPath + IMG);
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case HIT: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction((AbstractCreature)AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            }
            case GUARD: {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, GUARD_BLK));
                AbstractDungeon.actionManager.addToBottom(new DamageAction((AbstractCreature)AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            }
        }
        
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    
    @Override
    protected void getMove(int num) {
        // 10% chance to guard each turn, always guards first two turns and if it hasn't in the past 2 turns
        if (num >= 90 || !this.lastTwoMoves(GUARD)) {
            
            setMove(GUARD, AbstractMonster.Intent.ATTACK_DEFEND, GUARD_DMG);
        }
        // Otherwise hit
        else {
            setMove(HIT, AbstractMonster.Intent.ATTACK, HIT_DMG);
        }
    }
    
    @Override
    public void die() {
        super.die();
    }
}