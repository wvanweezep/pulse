package com.wvanw.pulse.prefabs;

import com.wvanw.pulse.core.IRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class PrefabRegistry implements IRegistry<Prefab> {

    private final Map<String, Prefab> prefabs = new HashMap<>();

    public PrefabRegistry() {}

    /**
     * Registers a new Prefab to the registry.
     * @param name unique name of the Prefab
     * @param prefab prefab to register
     * @throws IllegalArgumentException if a Prefab with
     * the provided name is already registered.
     */
    public void register(String name, Prefab prefab) {
        if (prefabs.containsKey(name)) throw new IllegalArgumentException(
                "Prefab with this name already exists: " + name);
        prefabs.put(name, prefab);
    }

    /**
     * Getter for a Prefab with a specified name.
     * @param name unique name of the Prefab
     * @return Prefab with the provided name
     * @throws NoSuchElementException if no Prefab with the provided name is found.
     */
    public Prefab get(String name) {
        if (!prefabs.containsKey(name)) throw new NoSuchElementException(
                "Unable to find Prefab with name: " + name);
        return prefabs.get(name);
    }

    /**
     * Getter for the size of the registry.
     * @return number of prefabs registered
     */
    public int size() {
        return prefabs.size();
    }

    public void clear() {
        prefabs.clear();
    }

    /**
     * Reloads all registered Prefabs.
     */
    public void reload() {
        // TODO: Re-implement with new pipeline
    }
}
