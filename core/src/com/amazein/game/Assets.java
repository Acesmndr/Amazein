package com.amazein.game;

import com.amazein.game.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by acesmndr on 8/25/15.
 */
public class Assets implements Disposable,AssetErrorListener {
    public static final String TAG=Assets.class.getName();
    public static final Assets instance=new Assets();
    public class AssetBatman {
        public final TextureAtlas.AtlasRegion bat;
        public AssetBatman (TextureAtlas atlas) {
            bat = atlas.findRegion("batplayer");
        }
    }
    public class AssetHouse {
        public final TextureAtlas.AtlasRegion house;
        public AssetHouse(TextureAtlas atlas){
            house = atlas.findRegion("house");
        }
    }
    public AssetManager assetManager;
    public AssetBatman acesBat;
    public AssetHouse blockadeHouse;

    private Assets(){

    };
    public void init(AssetManager assetManager){
        this.assetManager=assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "# of assets loaded: "
                + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);
        TextureAtlas atlas =
                assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
// enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        acesBat=new AssetBatman(atlas);
        blockadeHouse=new AssetHouse(atlas);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '"
                + asset.fileName + "'", (Exception)throwable);
    }

}
