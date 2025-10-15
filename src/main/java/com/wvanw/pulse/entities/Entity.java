package com.wvanw.pulse.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entity {

    private final EntityManager entityManager;

    private final UUID id;
    private String name;
    private boolean destroyed = false;

    protected final List<Component> components = new ArrayList<>();

    public Entity(EntityManager entityManager, String name) {
        this.entityManager = entityManager;
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
        this.name = entityManager.registerRename(this, name);
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
        entityManager.destroy(this);
    }

    // TODO: Implement copy method for instantiation
    public void copy() {

    }
}
