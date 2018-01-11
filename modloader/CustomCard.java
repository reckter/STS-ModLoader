package modloader;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import modloader.ModLoader;

public abstract class CustomCard extends AbstractCard {
    public CustomCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, int cardPool) {
        super(id, name, "status/beta", "status/beta", cost, rawDescription, type, color, rarity, target, cardPool);

        loadCardImage(img);
    }
    
    // loadCardImage - Hack to replace AbstractCard.portrait with a custom loaded image
    public void loadCardImage(String img) {
        Texture cardTexture = new Texture(img);
        cardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int tw = cardTexture.getWidth();
        int th = cardTexture.getHeight();
        TextureAtlas.AtlasRegion cardImg = new TextureAtlas.AtlasRegion(cardTexture, 0, 0, tw, th);
        ModLoader.setPrivateInherited(this, CustomCard.class, "portrait", cardImg);  
    }
}