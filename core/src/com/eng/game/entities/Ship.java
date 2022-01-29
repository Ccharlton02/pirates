package com.eng.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.eng.game.items.Cannon;
import com.eng.game.map.BackgroundTiledMap;

import static com.badlogic.gdx.math.Intersector.intersectLinePolygon;
import static com.badlogic.gdx.math.Intersector.intersectSegmentPolygon;

public class Ship extends Entity {

    public final float speed = 175;
    private final Texture texture;
    private final BackgroundTiledMap backgroundTiledMap;
    public Vector2 velocity = new Vector2();
    private float increment;

    public Ship(BackgroundTiledMap tiledMap, Texture texture, int health, int holdingCapacity, Integer movementRange, BackgroundTiledMap backgroundTiledMap) {
        super(tiledMap, texture, health, holdingCapacity);
        this.texture = texture;
        this.backgroundTiledMap = backgroundTiledMap;
        this.movementRange = movementRange;
        addItem(new Cannon(10, 6, 2));
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float oldX = getX(), oldY = getY();
        boolean collisionX = false, collisionY = false;

        setX(getX() + velocity.x * delta);

        // calculate the increment for collidesLeft and collidesRight
        increment = backgroundTiledMap.getTileWidth();
        increment = getWidth() < increment ? getWidth() / 2 : increment / 2;

        collisionX = backgroundTiledMap.newCollidesX(this);

//        if (velocity.x < 0) // going left
//            collisionX = collidesLeft();
//        else if (velocity.x > 0) // going right
//            collisionX = collidesRight();

        // react to x collision
        if (collisionX) {
            setX(oldX);
            velocity.x = 0;
        }

        // move on y
        setY(getY() + velocity.y * delta);


        // calculate the increment for collidesBottom and collidesTop
        increment = backgroundTiledMap.getTileHeight();
        increment = getHeight() < increment ? getHeight() / 2 : increment / 2;


        collisionY = backgroundTiledMap.newCollidesY(this);
//        if (velocity.y < 0) // going down
//            collisionY = collidesBottom();
//        else if (velocity.y > 0) // going up
//            collisionY = collidesTop();

//         react to y collision
        if (collisionY) {
            setY(oldY);
            velocity.y = 0;
        }

    }

    public boolean collidesRight() {
        for (float i = 0; i <= getHeight(); i += increment)
            if (backgroundTiledMap.isCellBlocked(getX() + getWidth(), getY() + i)) return true;
        return false;
    }

    public boolean collidesLeft() {
        for (float i = 0; i <= getHeight(); i += increment)
            if (backgroundTiledMap.isCellBlocked(getX(), getY() + i)) return true;
        return false;
    }

    public boolean collidesTop() {
        for (float i = 0; i <= getWidth(); i += increment)
            if (backgroundTiledMap.isCellBlocked(getX() + i, getY() + getHeight())) return true;
        return false;
    }

    public boolean collidesBottom() {
        for (float i = 0; i <= getWidth(); i += increment)
            if (backgroundTiledMap.isCellBlocked(getX() + i, getY())) return true;
        return false;
    }



    public String toString() {
        return "Ship";
    }
}
