package com.mygdx.game.HUD;

import static java.util.Map.entry;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Helpers;
import com.mygdx.game.Utils.IntPair;

import java.util.Map;

public class PossibleMoveImageCreator extends Group {
    Image emptyGrid = new Image(Helpers.getAssetManager().get("texturePacks/battleTextures.atlas",TextureAtlas .class).findRegion("emptygrid"));

    Map<Integer, Float> xCoords = Map.ofEntries(
            entry(0,0f ),
            entry(1, 41f),
            entry(2, 82f),
            entry(3, 123f),
            entry(4, 164f)
    );

    Map<Integer, Float> yCoords = Map.ofEntries(
            entry(0,0f ),
            entry(1, 41f),
            entry(2, 82f),
            entry(3, 123f),
            entry(4, 164f)
    );

    public PossibleMoveImageCreator(MoveSet moveSetToMap) {
        this.addActor(emptyGrid);
        for (IntPair possibleMove: moveSetToMap.possibleMoves){
            Image player = new Image(Helpers.getTextureRegionFromTextureAtlas("black_player"));
            player.setScale(0.5625f);
            player.setPosition(xCoords.get(2+possibleMove.xVal), yCoords.get(2+possibleMove.yVal));
            this.addActor(player);
        }
        Image player = new Image(Helpers.getTextureRegionFromTextureAtlas("green_player"));
        player.setScale(0.5625f);
        player.setPosition(xCoords.get(2), yCoords.get(2));
        this.addActor(player);
    }
}
