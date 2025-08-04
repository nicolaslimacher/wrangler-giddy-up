package com.mygdx.game.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.tommyettinger.textra.FWSkin;
import com.mygdx.game.Manager.BattleManager;
import com.mygdx.game.Manager.PRNGManager;
import com.mygdx.game.Screens.BattleScreen;
import com.mygdx.game.WranglerGiddyUp;

public class Helpers {
    private static final String TAG = Helpers.class.getSimpleName();

    /**
     * keeps TextButtons functioning as info pop-ups over game board so they are visible and not interfering
     * with moveset UI
     *
     * @param button button to be used as pop-up, following params refer to buttons position and dimension
     * @param desiredX
     * @param desiredY
     * @param desiredWidth
     * @param desiredHeight
     */

    public static void keepPopUpOverBoard(TextButton button, float desiredX, float desiredY, float desiredWidth, float desiredHeight){
        float finalX, finalY;

        if (desiredX + desiredWidth > Constants.SCREEN_WIDTH * 0.9f){
            finalX = Constants.SCREEN_WIDTH * 0.9f - desiredWidth;
        } else finalX = Math.max(desiredX, Constants.SCREEN_BOARD_WIDTH_LEFT_OFFSET * 2f);

        if (desiredY + desiredHeight > Constants.SCREEN_HEIGHT * 0.9f){
            finalY = Constants.SCREEN_HEIGHT * 0.9f - desiredHeight;
        } else finalY = Math.max(desiredY, Constants.SCREEN_HEIGHT * 0.1f);

        button.setBounds(finalX, finalY, desiredWidth, desiredHeight);
    }

    public static BattleScreen getGameScreen(){
        //may god have mercy on my soul for this abomination
        return ((BattleScreen) ((WranglerGiddyUp) Gdx.app.getApplicationListener()).getScreen());
    }

    /**
     * only call in game screen
     *
     * @return BattleManager handling current battle
     */
    public static BattleManager getCurrentBattleManager(){
        Gdx.app.debug(TAG, "Getting Battle Manager");
        //ONLY CALL IN GAME SCREEN
        return ((WranglerGiddyUp) Gdx.app.getApplicationListener()).getRunManager().getCurrentBattleManager();
    }

    public static PRNGManager getPRNGManager(){
        Gdx.app.debug(TAG, "Getting PRNG Manager");
        return ((WranglerGiddyUp) Gdx.app.getApplicationListener()).getPrngManager();
    }

    public static AssetManager getAssetManager(){
        Gdx.app.debug(TAG, "Getting PRNG Manager");
        return ((WranglerGiddyUp) Gdx.app.getApplicationListener()).getAssetManager();
    }

    public static TextureRegion getTextureRegionFromTextureAtlas(String textureName){
        return ((WranglerGiddyUp) Gdx.app.getApplicationListener()).getAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion(textureName);
    }

    public static FWSkin getGameSkin(){
        return ((WranglerGiddyUp) Gdx.app.getApplicationListener()).getSkin();
    }

}
