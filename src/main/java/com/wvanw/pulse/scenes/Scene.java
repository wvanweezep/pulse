package com.wvanw.pulse.scenes;

import com.wvanw.pulse.entities.EntityManager;

public class Scene {

    private final String name;
    private final EntityManager entityManager;

    // TODO: Add custom filetype for scene loading
    public Scene(String name) {
        this.name = name;
        this.entityManager = new EntityManager();
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
