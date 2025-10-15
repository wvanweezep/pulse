package com.wvanw.pulse.prefabs;

import com.wvanw.pulse.components.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

        catch (IOException e) {
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

    private static Component parseComponent(String line, int lineNumber) {
        String[] parts = line.split(":");
        if (parts.length != 2) throw new PrefabParseException(
                "Expected format: '<ComponentType>: <Arguments...>' but got: '"
                + line + "' (line " + lineNumber + ")");
        return null;
    }

}

/*
Class<?> clazz = Class.forName("my.package." + componentName);
Component c = (Component) clazz.getDeclaredConstructor().newInstance();
for (Map.Entry<String,String> entry : params.entrySet()) {
    Field field = clazz.getDeclaredField(entry.getKey());
    field.setAccessible(true);
    field.set(c, parseValue(field.getType(), entry.getValue()));
}
 */
