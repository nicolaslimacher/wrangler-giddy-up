package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.tommyettinger.textra.FWSkin;
import com.mygdx.game.Manager.PRNGManager;
import com.mygdx.game.Manager.RunManager;
import com.mygdx.game.Screens.MainMenuScreen;
import com.mygdx.game.Utils.Constants;

public class WranglerGiddyUp extends Game{
    public Stage stage;
    private AssetManager assetManager;
    private PRNGManager prngManager;
    private RunManager runManager;
    private FWSkin skin;


    public void create() {
        stage = new Stage(new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        assetManager = new AssetManager();
        prngManager = new PRNGManager();
        runManager = new RunManager();

        skin = new FWSkin(Gdx.files.internal("skins/ttfskin.json"));

        //add methods for creating and disposing screens as needed
        this.setScreen(new MainMenuScreen(this));
    }

    public AssetManager getAssetManager(){
        return assetManager;
    }
    public PRNGManager getPrngManager() {return prngManager;}
    public RunManager getRunManager() {return runManager;}
    public FWSkin getSkin(){return skin;}

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        stage.dispose();
    }

}