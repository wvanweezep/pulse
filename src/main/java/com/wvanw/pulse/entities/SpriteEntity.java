package com.wvanw.pulse.entities;

import com.wvanw.pulse.graphics.Sprite;
import com.wvanw.pulse.math.Vector2;

public abstract class SpriteEntity extends Entity {

    protected Sprite sprite;

    public SpriteEntity(String path, Vector2 position, Vector2 scale) {
        super(position, scale);
        this.sprite = new Sprite(path);
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void render() {
        sprite.render(position, scale);
    }
}
