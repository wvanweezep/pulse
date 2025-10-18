package com.wvanw.pulse.prefabs;

import com.wvanw.pulse.components.Component;
import com.wvanw.pulse.entities.Entity;
import com.wvanw.pulse.entities.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class Prefab {

    private final String name;
    private final List<Component> components = new ArrayList<>();

    public Prefab(String name, List<Component> components) {
        this.name = name;
        this.components.addAll(components);
    }

    public String getName() {
        return name;
    }

    public List<Component> getComponents() {
        return components;
    }

    public Entity createEntity(EntityManager entityManager, String name) {
        Entity entity = new Entity(entityManager, name);
        for (Component component : components)
            entity.addComponent(component);
        return entity;
    }
}
