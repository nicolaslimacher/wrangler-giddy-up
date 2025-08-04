package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.tommyettinger.textra.FWSkin;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.WranglerGiddyUp;
import com.mygdx.game.Utils.Helpers;

public class CharacterSelectScreen implements Screen{
    private static final String TAG = CharacterSelectScreen.class.getSimpleName();

    final WranglerGiddyUp game;
    SpriteBatch batch;
    Stage stage;
    FWSkin skin = Helpers.getGameSkin();
    private final TextureRegion starryBackground;

    Table buttons, seedTable;
    TextButton button1, button2, button3;
    Label seedInfoLabel, seedFeedbackLabel;
    TextButton seedConfirmButton, seedClearButton;
    TextField seedInput;

    private final String usingRandomSeedNotification = "Using new random seed";

    public CharacterSelectScreen(final WranglerGiddyUp game, final Stage stage) {
        this.game = game;
        this.stage = stage;
        Gdx.input.setInputProcessor(stage);

        starryBackground = game.getAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion("starrybackground");

        batch = new SpriteBatch();

        buttons = new Table();
        buttons.defaults().space(10f);
        buttons.setWidth(Constants.SCREEN_WIDTH*0.8f);
        buttons.setHeight(Constants.SCREEN_HEIGHT*0.5f);
        buttons.setPosition(Constants.SCREEN_WIDTH*0.1f, Constants.SCREEN_HEIGHT*0.4f);
        buttons.setDebug(true);
        stage.addActor(buttons);

        button1 = new TextButton("Click for\nGreen King", skin);
        button1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.log(TAG,"button1 selected");//used later to change player king
                game.setScreen(new BattleScreen(game, game.stage, 3));
                dispose();
            }
        });
        buttons.add(button1);

        button2 = new TextButton("Click for\nWhite King",skin);
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.log(TAG,"button2 selected");
                game.setScreen(new BattleScreen(game, game.stage, 2));
                dispose();
            }
        });
        buttons.add(button2);

        button3 = new TextButton("Click for\nBlack King",skin);
        button3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.log(TAG,"button3 selected");
                game.setScreen(new BattleScreen(game, game.stage, 1));
                dispose();
            }
        });
        buttons.add(button3);

        seedTable = new Table();
        seedTable.defaults().space(10f);
        seedTable.setWidth(Constants.SCREEN_WIDTH*0.8f);
        seedTable.setHeight(Constants.SCREEN_HEIGHT*0.3f);
        seedTable.setPosition(Constants.SCREEN_WIDTH*0.1f, Constants.SCREEN_HEIGHT*0.1f);
        seedTable.setDebug(true);
        stage.addActor(seedTable);


        seedInfoLabel = new Label("Set a custom seed if you want", skin);
        seedTable.add(seedInfoLabel);

        seedTable.row();

        seedInput = new TextField("", skin);
        seedInput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.log(TAG,"seed input text field received input, text:" + seedInput.getText());
                if (!seedInput.getText().isEmpty()) {
                    seedConfirmButton.setDisabled(seedInput.getText().length() != 19 || !isDigitsOnly(seedInput.getText()));
                }
                Gdx.app.debug(TAG, "confirm button disabled? " + seedConfirmButton.isDisabled());
            }
        });
        seedTable.add(seedInput).width(250f);

        seedConfirmButton = new TextButton("Confirm", skin);
        seedConfirmButton.setDisabled(true);
        seedConfirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.log(TAG,"seed confirm button selected");
                Helpers.getPRNGManager().setSeedToPlayerSeed(seedInput.getText());
                seedFeedbackLabel.setText("seed: " + seedInput.getText());
                seedClearButton.setVisible(true);
            }
        });
        seedTable.add(seedConfirmButton);

        seedTable.row();
        seedFeedbackLabel = new Label(usingRandomSeedNotification, skin);
        seedTable.add(seedFeedbackLabel);

        seedClearButton = new TextButton("X", skin);
        seedClearButton.setVisible(false);
        seedClearButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.log(TAG,"seed clear button clicked");
                Helpers.getPRNGManager().revertPlayerSeed();
                seedFeedbackLabel.setText(usingRandomSeedNotification);
            }
        });
        seedTable.add(seedClearButton);

    }

    @Override
    public void show() {

    }

    private void DeleteCharacterScreenWidgets(){
        button1.remove();
        button2.remove();
        button3.remove();
        buttons.remove();

        seedInfoLabel.remove();
        seedFeedbackLabel.remove();
        seedConfirmButton.remove();
        seedClearButton.remove();
        seedInput.remove();
        seedTable.remove();
    }

    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(0.10f, 0.10f, 0.15f, 1f);
        stage.act(deltaTime);

        // begin a new batch and draw board
        batch.begin();

        //for performance reasons disable blend before drawing background
        batch.disableBlending();
        batch.draw(starryBackground,0,0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        batch.enableBlending();


        stage.draw();
        batch.end();
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
        batch.dispose();
        DeleteCharacterScreenWidgets();
    }

    private boolean isDigitsOnly(String string){
        Gdx.app.debug(TAG, "checking that " + string + " is digits only: " + string.chars().allMatch(Character::isDigit));
        return string.chars().allMatch(Character::isDigit);
    }

}
