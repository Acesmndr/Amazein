package com.amazein.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Amazein implements ApplicationListener { //extends ApplicationAdapter
	private static final String TAG=Amazein.class.getName();
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	/*SpriteBatch batch;
	Texture img;*/
	boolean paused;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		worldController=new WorldController();
		worldRenderer=new WorldRenderer(worldController);
		paused = false;
		/*batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");*/
	}

	@Override
	public void render () {
		if(!paused) {
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		worldRenderer.render();
		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		batch.draw(img, 200, 200);
		batch.end();*/
	}

	@Override
	//public void create(){}
	public void resize(int width,int height){
		worldRenderer.resize(width,height);
	}
	public void pause(){
		paused=true;
	}
	public void resume(){
		paused=false;
	}
	public void dispose(){
		worldRenderer.dispose();
		Assets.instance.dispose();
	}
}
