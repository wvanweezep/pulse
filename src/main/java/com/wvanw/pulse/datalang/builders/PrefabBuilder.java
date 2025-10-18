package com.wvanw.pulse.datalang.builders;

import com.wvanw.pulse.components.Component;
import com.wvanw.pulse.components.ComponentRegistry;
import com.wvanw.pulse.datalang.dto.ParsedObject;
import com.wvanw.pulse.datalang.exceptions.ReflectionException;
import com.wvanw.pulse.prefabs.Prefab;
import com.wvanw.pulse.prefabs.PrefabParseException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

public class PrefabBuilder implements IBuilder<Prefab> {

    private final ParsedObject parsedObject;
    private final ComponentRegistry registry;

    public PrefabBuilder(ParsedObject parsedObject, ComponentRegistry registry) {
        this.parsedObject = parsedObject;
        this.registry = registry;
    }

    @Override
    @SuppressWarnings("unchecked") // StructuredParser enforces this behaviour
    public Prefab build() {
        List<Component> components = new ArrayList<>();
        for (Entry<String, Object> entry : parsedObject.objects().entrySet()) {
            Class<? extends Component> compClazz = registry.get(entry.getKey());
            components.add(compClazz.cast(instantiateObject(
                    compClazz, (Map<String, Object>) entry.getValue()
            )));
        }
        return new Prefab(parsedObject.name(), components);
    }

    private Object instantiateObject(Class<?> clazz, Map<String, Object> fields) {
        Object obj;
        try { obj = clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException _) {
            throw new ReflectionException("Failed to instantiate " + clazz.getSimpleName() +
                    ": ensure a public parameterless constructor exists.");
        }

        for (Entry<String, Object> entry : fields.entrySet()) {
            try {
                Field field = findField(clazz, entry.getKey());
                field.setAccessible(true);
                field.set(obj, buildValue(field.getType(), entry.getValue()));
            } catch (NoSuchFieldException _) {
                throw new ReflectionException("Unable to find field with name "
                        + entry.getKey() + " for " + clazz.getSimpleName());
            } catch (IllegalAccessException | ReflectionException e) {
                throw new ReflectionException(e.getMessage());
            }
        }
        return obj;
    }

    private Field findField(Class<?> clazz, String name) throws NoSuchFieldException {
        while (clazz != null) {
            try { return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                clazz = clazz.getSuperclass();
            }
        } throw new NoSuchFieldException(name);
    }

    @SuppressWarnings("unchecked")
    private <T> T buildValue(Class<T> clazz, Object value) {
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            return clazz.cast(instantiateObject(clazz, map));
        }
        if (!(value instanceof String str))
            throw new ReflectionException("Unexpected non-string, non-map value type: " +
                    (value == null ? "null" : value.getClass().getSimpleName()));

        if (clazz == String.class)
            return (T) value;
        if (clazz == int.class || clazz == Integer.class)
            return (T) (Integer) Integer.parseInt(str);
        if (clazz == float.class || clazz == Float.class)
            return (T) (Float) Float.parseFloat(str);
        if (clazz == double.class || clazz == Double.class)
            return (T) (Double) Double.parseDouble(str);
        if (clazz == long.class || clazz == Long.class)
            return (T) (Long) Long.parseLong(str);
        if (clazz == boolean.class || clazz == Boolean.class)
            return (T) (Boolean) Boolean.parseBoolean(str);

        throw new PrefabParseException("No implementation for parsing "
                + clazz.getSimpleName() + " found: " + value);
    }
}
