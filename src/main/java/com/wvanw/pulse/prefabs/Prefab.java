package com.wvanw.pulse.prefabs;

import com.wvanw.pulse.components.Component;
import com.wvanw.pulse.entities.Entity;
import com.wvanw.pulse.entities.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class Prefab {

    private final String path;
    private final List<Component> components = new ArrayList<>();

    public Prefab(String path, List<Component> components) {
        this.path = path;
        this.components.addAll(components);
    }

    public String getPath() {
        return path;
    }

    public Entity createEntity(EntityManager entityManager, String name) {
        Entity entity = new Entity(entityManager, name);
        for (Component component : components)
            entity.addComponent(component);
        return entity;
    }
}
