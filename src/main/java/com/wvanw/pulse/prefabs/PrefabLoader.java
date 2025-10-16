package com.wvanw.pulse.prefabs;

import com.wvanw.pulse.components.Component;
import com.wvanw.pulse.components.ComponentRegistry;
import com.wvanw.pulse.core.IParsable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class PrefabLoader {

    public static Prefab load(String path) {
        String prefabName = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            List<Component> components = new ArrayList<>();
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber += 1;
                if (line.isEmpty()) continue;
                if (prefabName == null) prefabName = parseName(line.strip(), lineNumber);
                else components.add(parseComponent(line.strip(), lineNumber));
            } return new Prefab(path, components);
        }

        catch (PrefabParseException e) {
            if (prefabName != null)
                throw new PrefabParseException("Error parsing prefab '" + prefabName + "': " + e.getMessage());
            throw new PrefabParseException("Error parsing prefab '" + path + "': " + e.getMessage());
        }

        catch (Exception e) {
            if (prefabName != null)
                throw new PrefabParseException("An Exception occurred while loading Prefab: " + prefabName);
            throw new PrefabParseException("An Exception occurred while loading Prefab: " + path);
        }
    }

    private static String parseName(String line, int lineNumber) {
        String[] parts = line.split(" ");
        if (parts.length != 2 || !parts[0].equals("Prefab"))
            throw new PrefabParseException("Expected format: 'Prefab <name>' but got: '"
                    + line + "' (line " + lineNumber + ")");
        return parts[1];
    }

    private static Component parseComponent(String line, int lineNumber) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        String[] parts = line.split(":");
        if (parts.length != 2) throw new PrefabParseException(
                "Expected format: '<ComponentType>: <Arguments...>' but got: '"
                 + line + "' (line " + lineNumber + ")");
        Map<String, String> parameterMap = parseParameters(line, lineNumber, parts[1]);

        Class<? extends Component> clazz = ComponentRegistry.getInstance().get(parts[0]);
        Component c = clazz.getDeclaredConstructor().newInstance();
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            Field field = clazz.getDeclaredField(entry.getKey());
            field.setAccessible(true);
            field.set(c, parseValue(field.getType(), entry.getValue()));
        }
        return c;
    }

    private static Map<String, String> parseParameters(String line, int lineNumber, String paramString) {
        String[] parameters = paramString.strip().split(",");
        Map<String, String> parameterMap = new HashMap<>();
        for (String param : parameters) {
            String[] symbolValue = param.strip().split("=");
            if (symbolValue.length != 2) throw new PrefabParseException(
                    "Expected format: '<ParameterName>=<Value>' but got '"
                    + param + "' (line " + lineNumber + ")");
            parameterMap.put(symbolValue[0].strip(), symbolValue[1].strip());
        }
        return parameterMap;
    }

    private static <T> T parseValue(Class<T> clazz, String value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (clazz == String.class)
            return (T) value;
        else if (IParsable.class.isAssignableFrom(clazz)) {
            Method parseMethod = clazz.getMethod("parse", String.class);
            return (T) parseMethod.invoke(value, value);
        }
        else if (clazz == int.class || clazz == Integer.class)
            return (T) (Integer) Integer.parseInt(value);
        else if (clazz == float.class || clazz == Float.class)
            return (T) (Float) Float.parseFloat(value);
        else if (clazz == double.class || clazz == Double.class)
            return (T) (Double) Double.parseDouble(value);
        else throw new PrefabParseException("No implementation for parsing "
                    + clazz.getSimpleName() + " found: " + value);
    }
}
