package com.wvanw.pulse.entities;

import java.util.Optional;
import java.util.UUID;

public class EntityHandle {

    private final EntityManager entityManager;
    private final UUID id;

    public EntityHandle(EntityManager entityManager, UUID id) {
        this.entityManager = entityManager;
        this.id = id;
    }

    public Entity get() {
        return entityManager.get(id);
    }

    public Optional<Entity> tryGet() {
        return entityManager.tryGet(id);
    }

    public boolean exists() {
        return tryGet().isPresent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityHandle h)) return false;
        return id.equals(h.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        Optional<Entity> e = tryGet();
        return e.map(entity -> entity.getClass().getSimpleName() + '[' + id + ']')
                .orElseGet(() -> "NotFound[" + id + ']');
    }
}
