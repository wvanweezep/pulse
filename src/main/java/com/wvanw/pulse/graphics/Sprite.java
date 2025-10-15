package com.wvanw.pulse.graphics;

import com.wvanw.pulse.math.Vector2;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Class for storing a texture and simplifying rendering.
 */
public class Sprite {

    private static final String ASSETS_PATH = "/com/wvanw/pulse/assets/";
    private static final HashMap<String, Image> CACHE = new HashMap<>();
    private final Image sprite;

    /**
     * Creates a new instance of a Sprite with an image found on the specified path.
     * Uses a cache map to avoid reloading images.
     * @param path the relative path of the image
     */
    public Sprite(String path) {
        this.sprite = CACHE.computeIfAbsent(path, Sprite::loadImage);
    }

    /**
     * Loads an image with a specified path and returns it.
     * @param path the relative path of the image
     * @return newly loaded image from specified path
     * @throws IllegalArgumentException if the sprite could not be found.
     */
    private static Image loadImage(String path) {
        InputStream stream = Sprite.class.getResourceAsStream(ASSETS_PATH + path);
        if (stream == null) throw new IllegalArgumentException(
                "Sprite resource not found: " + path);
        return new Image(stream);
    }

    /**
     * Getter for the width of the Sprite.
     * @return width of the Sprite
     */
    public double getWidth() {
        return sprite.getWidth();
    }

    /**
     * Getter for the height of the Sprite.
     * @return height of the Sprite
     */
    public double getHeight() {
        return sprite.getHeight();
    }

    /**
     * Queues a render call for this Sprite to be rendered on the next frame.
     * @param x x position on the canvas
     * @param y y position on the canvas
     * @param scaleX factor to scale the sprite's width
     * @param scaleY factor to scale the sprite's height
     */
    public void render(Vector2 position, Vector2 scale) {
        RenderManager.getInstance().queue(gc ->
                gc.drawImage(sprite, position.x, position.y,
                        sprite.getWidth() * scale.x,
                        sprite.getHeight() * scale.y));
    }
}
