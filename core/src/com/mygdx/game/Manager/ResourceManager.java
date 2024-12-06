package com.mygdx.game.Manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.HUD.SkinLoader;
import com.mygdx.game.HUD.TTFSkin;

import java.util.List;
import java.util.Map;

public class ResourceManager extends AssetManager {
    public ResourceManager() {
        super();

        final FileHandleResolver resolver = getFileHandleResolver();
        setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        setLoader(TTFSkin.class, new SkinLoader(resolver));
        setLoader(TiledMap.class, new TmxMapLoader(resolver));
    }

    public TTFSkin loadSkinSynchronously(final String skinJsonFilePath, Map<String, List<Integer>> fontMap) {
        load(skinJsonFilePath, TTFSkin.class, new SkinLoader.SkinParameter(fontMap));
        finishLoading();
        return get(skinJsonFilePath, TTFSkin.class);
    }
}