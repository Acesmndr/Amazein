package com.amazein.game;

import com.amazein.game.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by acesmndr on 8/22/15.
 */
public class WorldRenderer implements Disposable {
        private OrthographicCamera cameraGUI;
        private OrthographicCamera camera;
        private SpriteBatch batch;
        private WorldController worldController;
        public WorldRenderer(WorldController worldController){
            this.worldController=worldController;
            init();
        }
        public void init(){
            batch=new SpriteBatch();
            camera=new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
            camera.position.set(0,0,0);
            camera.update();
            cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
                    Constants.VIEWPORT_GUI_HEIGHT);
            cameraGUI.position.set(0,0,0);
            cameraGUI.setToOrtho(true);
            cameraGUI.update();
        }
        public void render(){
            renderGui(batch);
            renderTestObjects();
        }
    private void renderGui (SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        batch.end();
    }
    private void renderTestObjects() {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Sprite sprite:worldController.testSprites){
            sprite.draw(batch);
        }
        /*for (Sprite sprite:worldController.rockSprites){
            sprite.draw(batch);
        }*/
        batch.end();
    }

    public void resize(int width,int height){
        camera.viewportWidth=(Constants.VIEWPORT_HEIGHT/height)*width;
        camera.update();
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT
                / (float)height) * (float)width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2,
                cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
