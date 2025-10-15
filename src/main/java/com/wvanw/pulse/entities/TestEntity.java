package com.wvanw.pulse.entities;

import com.wvanw.pulse.math.Vector2;

public class TestEntity extends SpriteEntity {

    public TestEntity(Vector2 position) {
        super("Twitter-Icon.png", position, new Vector2(1, 1));
    }

    @Override
    public void update() {
        super.fixedUpdate();
        position.x += 1;
    }
}
