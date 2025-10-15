package com.wvanw.pulse.ecs;

import com.wvanw.pulse.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Entity {

    private final UUID id;
    private String name;
    private boolean destroyed = false;

    protected final List<Component> components = new ArrayList<>();

    public Entity(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String setName(String name) {
        this.name = EntityManager.getInstance().registerRename(this, name);
        return this.name;
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

    public void destroy() {
        if (destroyed) return;
        destroyed = true;
        EntityManager.getInstance().destroy(this);
    }
}
