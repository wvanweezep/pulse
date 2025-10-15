package com.wvanw.pulse.scenes;

import com.wvanw.pulse.entities.EntityManager;
import com.wvanw.pulse.prefabs.PrefabRegistry;

public class Scene {

    private final String name;
    private final EntityManager entityManager;
    private final PrefabRegistry prefabRegistry;

    // TODO: Add custom filetype for scene loading
    public Scene(String name, PrefabRegistry prefabRegistry) {
        this.name = name;
        this.entityManager = new EntityManager();
        this.prefabRegistry = prefabRegistry;
    }

    public void load() {

    }

    /**
     * Invokes the update call on the EntityManager every frame.
     */
    public void update() {
        entityManager.update();
    }

    /**
     * Invokes the fixedUpdate call on the EntityManager based on the set physics fps.
     */
    public void fixedUpdate() {
        entityManager.fixedUpdate();
    }

    /**
     * Invokes the render call on the EntityManger every frame.
     */
    public void render() {
        entityManager.render();
    }
}
