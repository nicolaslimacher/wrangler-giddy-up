package com.mygdx.game.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.WranglerGiddyUp;

public class MainMenuScreen implements Screen{
    private static final String TAG = MainMenuScreen.class.getSimpleName();
    final WranglerGiddyUp game;
    Stage stage;
    OrthographicCamera camera;
    SpriteBatch batch;
    TextureRegion loadingScreen;

    public MainMenuScreen(final WranglerGiddyUp game) {
        this.game = game;
        stage = game.stage;
        batch = new SpriteBatch();

        camera = new OrthographicCamera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        camera.setToOrtho(false, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        loadingScreen = new TextureRegion(new Texture(Gdx.files.internal("loading_screen.png")));

        game.getAssetManager().load("texturePacks/battleTextures.atlas", TextureAtlas.class);
        Gdx.app.log("MainMenu", "Game Started");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        //set log level of app (ship in info?)
        Gdx.app.setLogLevel(Application.LOG_DEBUG);


        camera.update();
        batch.setProjectionMatrix(camera.combined);

        //begin new sprite batch and draw welcome (need game. before methods)
        batch.begin();
        batch.draw(loadingScreen, 0,0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        batch.end();

        //only true if resource manager finishes loading async assets
        if(game.getAssetManager().update()) {
            if (Gdx.input.isTouched()) {
                //game.setScreen(new BattleScreen(game, game.stage));
                game.setScreen(new CharacterSelectScreen(game, game.stage));
                Gdx.app.log("MainMenu", "Input received, creating BattleScreen instance.");
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        Gdx.app.debug(TAG, "Resizing, new screen width: " + Gdx.graphics.getWidth() + ", height: " + Gdx.graphics.getHeight());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Gdx.app.log("MainMenu", "Dispose called.");
    }
}
