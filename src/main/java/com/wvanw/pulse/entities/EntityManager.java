package com.wvanw.pulse.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityManager {

    private final static EntityManager INSTANCE = new EntityManager();
    private final Map<String, Entity> entities = new HashMap<>();

    private EntityManager() {}

    public static EntityManager getInstance() {
        return INSTANCE;
    }

    private String generateName(String base) {
        if (!entities.containsKey(base)) return base;
        int i = 1;
        while (true) {
            String name = base + " (" + i + ")";
            if (!entities.containsKey(name))
                return name;
            i++;
        }
    }

    public void addEntity(String name, Entity entity) {
        entities.put(generateName(name), entity);
    }

    public void update() {
        for (Entity entity : entities.values())
            entity.update();
    }

    public void fixedUpdate() {
        for (Entity entity : entities.values())
            entity.fixedUpdate();
    }

    public void render() {
        for (Entity entity : entities.values())
            entity.render();
    }
}
