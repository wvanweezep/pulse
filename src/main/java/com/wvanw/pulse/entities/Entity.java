package com.wvanw.pulse.entities;

import com.wvanw.pulse.components.Component;
import com.wvanw.pulse.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {

    protected final Vector2 position;
    protected final Vector2 scale;
    protected final List<Component> components = new ArrayList<>();

    public Entity(Vector2 position, Vector2 scale) {
        this.position = position;
        this.scale = scale;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getScale() {
        return scale;
    }

    // TODO: Implement Component control methods (getComponent, addComponent, ...)

    public void update() {
        for (Component component : components)
            component.update();
    }

    public void fixedUpdate() {
        for (Component component : components)
            component.fixedUpdate();
    }

    public void render() {}
}
