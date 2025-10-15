package com.wvanw.pulse.prefabs;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class PrefabRegistry {

    private final Map<String, Prefab> prefabs = new HashMap<>();

    public PrefabRegistry() {}

    /**
     * Registers a new Prefab to the registry.
     * @param name unique name of the Prefab
     * @param path path of the .prefab file
     * @throws IllegalArgumentException if a Prefab with
     * the provided name is already registered.
     */
    public void register(String name, String path) {
        if (prefabs.containsKey(name)) throw new IllegalArgumentException(
                "Prefab with this name already exists: " + name);
        prefabs.put(name, PrefabLoader.load(path));
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
     * Reloads all registered Prefabs.
     */
    public void reload() {
        prefabs.replaceAll((k, _) ->
                PrefabLoader.load(prefabs.get(k).getPath())
        );
    }
}
