package com.wvanw.pulse.components;

import com.wvanw.pulse.core.IRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ComponentRegistry implements IRegistry<Class<? extends Component>> {

    private static final ComponentRegistry INSTANCE = new ComponentRegistry();
    private final Map<String, Class<? extends Component>> components = new HashMap<>();

    public ComponentRegistry() {}

    public static ComponentRegistry getInstance() {
        return INSTANCE;
    }

    public void register(String name, Class<? extends Component> component) {
        if (components.containsKey(name)) throw new IllegalArgumentException(
                "Component with this name already exists: " + name);
        components.put(name, component);
    }

    public Class<? extends Component> get(String name) {
        if (!components.containsKey(name)) throw new NoSuchElementException(
                "Unable to find Component with name: " + name);
        return components.get(name);
    }

    public int size() {
        return components.size();
    }

    public void clear() {
        components.clear();
    }
}
