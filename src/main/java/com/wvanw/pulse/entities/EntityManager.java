package com.wvanw.pulse.entities;

import java.util.*;

public class EntityManager {

    private final Map<UUID, Entity> entities = new HashMap<>();
    private final Map<String, UUID> idLookup = new HashMap<>();
    private final Set<UUID> toDestroy = new HashSet<>();

    public EntityManager() {}

    /**
     * Generates a new scene-unique name based on a given base.
     * @param base starting point of the name
     * @return scene-unique Entity name
     */
    private String generateUniqueName(String base) {
        int i = 1;
        String name = base;
        while (idLookup.containsKey(name)) {
            name = base + " (" + i++ + ")";
        } return name;
    }

    /**
     * Registers a new Entity to the EntityManager.
     * @param entity Entity to register
     */
    public void register(Entity entity) {
        idLookup.put(entity.getName(), entity.getId());
        entities.put(entity.getId(), entity);
    }

    /**
     * Queues the destruction of an Entity at the end of the current update cycle.
     * @param entity Entity to remove
     */
    public void destroy(Entity entity) {
        toDestroy.add(entity.getId());
    }

    /**
     * Destroys all queued Entities in the {@code toDestroy} set.
     */
    private void processDestruction() {
        for (UUID id : toDestroy) {
            Entity e = entities.remove(id);
            if (e != null) idLookup.remove(e.getName());
        } toDestroy.clear();
    }

    /**
     * Registers a new scene-unique name for an already registered Entity.
     * @param entity renamed Entity
     * @param newName new name for the Entity
     * @return generated scene-unique Entity name
     */
    public String registerRename(Entity entity, String newName) {
        idLookup.remove(entity.getName());
        String newUniqueName = generateUniqueName(newName);
        idLookup.put(newUniqueName, entity.getId());
        return newUniqueName;
    }

    /**
     * Checks for the existence of an Entity with a provided id.
     * @param id UUID of the Entity
     * @return {@code true} if an Entity with the given id is found
     */
    public boolean contains(UUID id) {
        return entities.containsKey(id);
    }

    /**
     * Checks for the existence of an Entity with a provided name.
     * @param name scene-unique name of the Entity
     * @return {@code true} if an Entity with the given name is found
     */
    public boolean contains(String name) {
        return contains(idLookup.get(name));
    }

    /**
     * Attempts to retrieve an Entity with a provided id.
     * @param id UUID of the Entity
     * @return an {@code Optional}, possibly with the target Entity
     */
    public Optional<Entity> tryGet(UUID id) {
        return Optional.ofNullable(entities.get(id));
    }

    /**
     * Attempts to retrieve an Entity with a provided name.
     * @param name scene-unique name of the Entity
     * @return an {@code Optional}, possibly with the target Entity
     */
    public Optional<Entity> tryGet(String name) {
        return tryGet(idLookup.get(name));
    }

    /**
     * Retrieves an Entity with a provided id.
     * @param id UUID of the Entity
     * @return Entity with provided id
     * @throws NoSuchElementException if no Entity with the provided id is found.
     */
    public Entity get(UUID id) {
        if (!entities.containsKey(id)) throw new NoSuchElementException(
                "Unable to find Entity with id: " + id);
        return entities.get(id);
    }

    /**
     * Retrieves an Entity with a provided name.
     * @param name UUID of the Entity
     * @return Entity with provided name
     * @throws NoSuchElementException if no Entity with the provided name is found.
     */
    public Entity get(String name) {
        if (!idLookup.containsKey(name)) throw new NoSuchElementException(
                "Unable to find Entity with name: " + name);
        return get(idLookup.get(name));
    }

    /**
     * Invokes the update call on all Entities every frame.
     */
    public void update() {
        for (Entity entity : entities.values())
            entity.update();
        processDestruction();
    }

    /**
     * Invokes the fixedUpdate call on all Entities based on the set physics fps.
     */
    public void fixedUpdate() {
        for (Entity entity : entities.values())
            entity.fixedUpdate();
    }

    /**
     * Invokes the render call on all Entities every frame.
     */
    public void render() {
        for (Entity entity : entities.values())
            entity.render();
    }
}
