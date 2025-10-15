package com.wvanw.pulse.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;

public class Sprite {

    private static final String ASSETS_PATH = "/com/wvanw/pulse/assets/";
    private static final HashMap<String, Image> CACHE = new HashMap<>();
    private final Image sprite;

    public Sprite(String path) {
        this.sprite = CACHE.computeIfAbsent(path, Sprite::loadImage);
    }

    private static Image loadImage(String path) {
        InputStream stream = Sprite.class.getResourceAsStream(ASSETS_PATH + path);
        if (stream == null) throw new IllegalArgumentException(
                "Sprite resource not found: " + path);
        return new Image(stream);
    }

    public double getWidth() {
        return sprite.getWidth();
    }

    public double getHeight() {
        return sprite.getHeight();
    }

    public void render(GraphicsContext gc, int x, int y, double scaleX, double scaleY) {
        gc.drawImage(sprite, x, y, sprite.getWidth() * scaleX, sprite.getHeight() * scaleY);
    }
}
