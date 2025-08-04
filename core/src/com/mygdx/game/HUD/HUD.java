package com.mygdx.game.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.textra.FWSkin;
import com.github.tommyettinger.textra.TextraButton;
import com.mygdx.game.Manager.BattleManager;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.Helpers;

public class HUD implements Disposable {
    public CustomHUDStage customHUDStage;
    private Viewport viewport;
    private BattleManager battleManager;

    //Scene2D Widgets
    FWSkin skin = Helpers.getGameSkin();
    private final TextButton seedDisplay;
    private TextButton undoButton;
    private TextraButton endTurn;

    public HUD(SpriteBatch spriteBatch, BattleManager battleManager) {
        Gdx.app.log("HUD", "Creating HUD");
        viewport = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, new OrthographicCamera());
        customHUDStage = new CustomHUDStage(viewport, spriteBatch, battleManager);

        seedDisplay = new TextButton("Seed: " + Helpers.getPRNGManager().getOriginalSeed(), skin);
        seedDisplay.setBounds(Constants.SCREEN_WIDTH - 350 , Constants.SCREEN_HEIGHT-40, 335, 35);
        seedDisplay.setName("TurnCounterMenu");
        customHUDStage.addActor(seedDisplay);

        Table table = new Table();

        this.undoButton = new TextButton("UNDO", skin);
        undoButton.addListener(undoButtonListener);
        table.add(undoButton).expand().fill();
        DisableUndoButton(); //battle manager will enable when move command has been made

        this.endTurn = new TextraButton("END TURN", skin);
        endTurn.addListener(endTurnButtonListener);
        table.add(endTurn).expand().fill();
        DisableEndTurnButton(); //battle manager will enable when move command has been made

        table.setBounds(Constants.SCREEN_WIDTH - 400 , 5, 390, 35);
        table.defaults().padRight(10); // All cells have a padding of 10px to the right
        table.setName("UndoEndTurnMenu");
        customHUDStage.addActor(table);
    }

    private final InputListener undoButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("Played has hit undo");
            BattleManager battleManager = Helpers.getCurrentBattleManager();
            if (battleManager.latestGamePieceCommand != null){
                battleManager.latestGamePieceCommand.Undo();
            }
            DisableEndTurnButton();
            return true;
        }
    };

    private final InputListener endTurnButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("Player has ended turn");
            BattleManager battleManager = Helpers.getCurrentBattleManager();
            battleManager.EndPlayerTurn();
            return true;
        }
    };


    @Override
    public void dispose() {
        customHUDStage.dispose();
    }

    public void UpdateTurn (){
        //this.seedDisplay.setText("Turn: " + Helpers.getCurrentBattleManager().turnNumber);
    }
    public void EnableUndoButton(){
        Gdx.app.debug("HUD", "Undo button enabled");
        this.undoButton.setDisabled(false);
    }
    public void DisableUndoButton(){
        Gdx.app.debug("HUD", "Undo button disabled");
        this.undoButton.setDisabled(true);
    }

    public void EnableEndTurnButton(){
        Gdx.app.debug("HUD", "End button enabled");
        this.endTurn.setDisabled(false);
    }
    public void DisableEndTurnButton(){
        Gdx.app.debug("HUD", "End button disabled");
        this.endTurn.setDisabled(true);
    }
}
