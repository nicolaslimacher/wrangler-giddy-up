package com.mygdx.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.tommyettinger.textra.FWSkin;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.HUD.TTFSkin;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.Helpers;

public class DisplayMessage extends Table {
    private static final String TAG = DisplayMessage.class.getSimpleName();
    Label headerDisplay, subDisplay;
    FWSkin ttfSkin = Helpers.getGameSkin();

    public DisplayMessage(BattleManager battleManager, String headerMessage, String subMessage, float displayTime) {
        super();

        this.setBounds(Constants.SCREEN_WIDTH / 4f, Constants.SCREEN_HEIGHT * 0.25f, Constants.SCREEN_WIDTH / 2f, Constants.SCREEN_HEIGHT / 2f);

        //using glyphLayout to determine how wide and tall label will be
        headerDisplay = new Label(headerMessage, ttfSkin, "headerDisplayMessage");
        this.add(headerDisplay);
        this.row();
        Gdx.app.debug(TAG, "Display label created with text: " + headerDisplay.getText() + " at pos: " + headerDisplay.getX() + "," + headerDisplay.getY());

        subDisplay = new Label(subMessage, ttfSkin, "subDisplayMessage");
        this.add(subDisplay);
        Gdx.app.debug(TAG, "Sub Display label created with text: " + subDisplay.getText() + " at pos: " + subDisplay.getX() + "," + subDisplay.getY());

        battleManager.getStage().addActor(this);
        RunnableAction destroy = new RunnableAction();
        Action fadeOutAction = Actions.fadeOut(displayTime * 0.75f, Interpolation.fade);
        destroy.setRunnable(DisplayMessage.this::remove);
        this.addAction(new SequenceAction(
                new DelayAction(displayTime * 0.25f),
                fadeOutAction,
                destroy
        ));
    }

    public DisplayMessage(BattleManager battleManager, String headerMessage, float displayTime) {
        this(battleManager, headerMessage, "", displayTime);
    }
}