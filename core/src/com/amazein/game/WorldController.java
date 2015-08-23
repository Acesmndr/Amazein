package com.amazein.game;

import com.amazein.game.util.CameraHelper;
import com.amazein.game.util.Constants;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by acesmndr on 8/22/15.
 */
public class WorldController extends InputAdapter{
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    private boolean accelerometerAvailable;
    public CameraHelper cameraHelper;
    private static final String TAG=WorldController.class.getName();
    public Sprite[] testSprites,rockSprites;
    public int selectedSprite;
    public WorldController(){
        Gdx.input.setInputProcessor(this);
        init();
    }
    public void init(){
        accelerometerAvailable=Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        cameraHelper=new CameraHelper();
        initTestObjects();
    }

    private void initTestObjects() {
        testSprites=new Sprite[5];
        rockSprites=new Sprite[5];
        int height=24;
        int width=24;
        Texture img=new Texture(Gdx.files.internal("batplayer.png"));
        Pixmap pixmap=createProceduralPixmap(width,height);
        Texture texture=new Texture(pixmap);
        for(int i=0;i<testSprites.length;i++){
            Sprite spr=new Sprite(img);
            spr.setSize(1, 1);
            spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
            float randomX= MathUtils.random(-2.0f,2.0f);
            float randomY=MathUtils.random(-2.0f,2.0f);
            spr.setPosition(randomX,randomY);
            testSprites[i]=spr;

        }
        for(int i=0;i<testSprites.length;i++){
            Sprite spr1=new Sprite(texture);
            spr1.setSize(0.25f, 0.25f);
            spr1.setOrigin(spr1.getWidth()/2.0f,spr1.getHeight()/2.0f);
            float randomX= MathUtils.random(-2.0f,2.0f);
            float randomY=MathUtils.random(-2.0f,2.0f);
            spr1.setPosition(randomX+0.04f,randomY+0.04f);
            rockSprites[i]=spr1;
        }
        selectedSprite=0;
    }
    private Pixmap createProceduralPixmap(int width,int height){
        Pixmap pixmap=new Pixmap(width,height, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 0.5f);
        pixmap.fill();
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }
    public void update(float deltaTime){
        handleDebugInput(deltaTime);
        updateTestObjects(deltaTime);
        testCollisions();
        cameraHelper.update(deltaTime);
    }
    @Override
    public boolean keyUp (int keycode) {
// Reset game world
        if (keycode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
// Select next sprite
        else if (keycode == Input.Keys.SPACE) {
            selectedSprite = (selectedSprite + 1) % testSprites.length;
            if (cameraHelper.hasTarget()) {
                cameraHelper.setTarget(testSprites[selectedSprite]);
                r1.set(testSprites[selectedSprite].getX(),testSprites[selectedSprite].getY(),32,32);
            }
            Gdx.app.debug(TAG, "Sprite #" + selectedSprite + " selected");
        }
        else if (keycode == Input.Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null :
                    testSprites[selectedSprite]);
            Gdx.app.debug(TAG, "Camera follow enabled: " +
                    cameraHelper.hasTarget());
        }
        return false;
    }
    private void handleDebugInput(float deltaTime) {
        if (accelerometerAvailable) {
            float amountX = Gdx.input.getAccelerometerY() / 10.0f;
            float amountY = Gdx.input.getAccelerometerX() / 10.0f;
            amountX *= 90.0f;
            amountY *= 90.0f;
        if (Math.abs(amountX) < Constants.ACCEL_ANGLE_DEAD_ZONE) {
                amountX = 0;
            } else {
                    amountX /= Constants.ACCEL_MAX_ANGLE_MAX_MOVEMENT;
            }
            if (Math.abs(amountY) < Constants.ACCEL_ANGLE_DEAD_ZONE) {
                amountY = 0;
            } else {
                amountY /= Constants.ACCEL_MAX_ANGLE_MAX_MOVEMENT;
            }
            moveSelectedSprite(amountX * 0.02f, -amountY * 0.01f);
        if(Gdx.input.isTouched()){
                selectedSprite = (selectedSprite + 1) % testSprites.length;
                if (cameraHelper.hasTarget()) {
                    cameraHelper.setTarget(testSprites[selectedSprite]);
                }
                Gdx.app.debug(TAG, "Sprite #" + selectedSprite + " selected");
            }

        }
        /*if(Gdx.app.getType()!= Application.ApplicationType.Desktop)
            return;
        float sprMoveSpeed = 5 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) moveSelectedSprite(
                -sprMoveSpeed, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            moveSelectedSprite(sprMoveSpeed, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) moveSelectedSprite(0,
                sprMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) moveSelectedSprite(0,
                -sprMoveSpeed);
        float camMoveSpeed = 5 * deltaTime;
        float camMoveSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camMoveSpeed *=
                camMoveSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-camMoveSpeed,
                0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(camMoveSpeed,
                0);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, camMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0,
                -camMoveSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
            cameraHelper.setPosition(0, 0);
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *=
                camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA))
            cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(
                -camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);*/
    }
    private void testCollisions () {
        r1.set(testSprites[selectedSprite].getX(), testSprites[selectedSprite].getY(), 32, 32);
// Test collision: Bunny Head <-> Rocks
        for (Sprite rsprite : rockSprites) {
            r2.set(rsprite.getBoundingRectangle());
            if (!r1.overlaps(r2)) continue;
            onCollision();
// IMPORTANT: must do all collisions for valid
// edge testing on rocks.
        }
    }

    private void onCollision() {
        selectedSprite = (selectedSprite + 1) % testSprites.length;
        if (cameraHelper.hasTarget()) {
            cameraHelper.setTarget(testSprites[selectedSprite]);
        }
        Gdx.app.debug(TAG, "Sprite #" + selectedSprite + " selected");
    }

    private void moveCamera (float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    private void moveSelectedSprite (float x, float y) {
        testSprites[selectedSprite].translate(x, y);
    }

    private void updateTestObjects(float deltaTime){
        float rotation=testSprites[selectedSprite].getRotation();
        rotation+=90*deltaTime;
        rotation%=360;
        testSprites[selectedSprite].setRotation(rotation);
    }
}
