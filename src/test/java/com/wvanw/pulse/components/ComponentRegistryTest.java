package com.wvanw.pulse.components;

import com.wvanw.pulse.commons.TestComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.NoSuchElementException;

public class ComponentRegistryTest {

    private ComponentRegistry registry;

    @BeforeEach
    public void startUp() {
        registry = new ComponentRegistry();
    }

    @Test
    public void testRegister() {
        registry.register("TestComponent", TestComponent.class);
        Assertions.assertEquals(1, registry.size());
        registry.get("TestComponent");
    }

    @Test
    public void testMultiAliasRegister() {
        registry.register("TestComponent", TestComponent.class);
        registry.register("Test", TestComponent.class);
        Assertions.assertEquals(registry.get("TestComponent"), registry.get("Test"));
    }

    @Test
    public void testDuplicateNameRegister() {
        registry.register("TestComponent", TestComponent.class);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            registry.register("TestComponent", TestComponent.class);
        });
    }

    @Test
    public void testGetRegister() {
        registry.register("TestComponent", TestComponent.class);
        Assertions.assertEquals(TestComponent.class, registry.get("TestComponent"));
    }

    @Test
    public void testUnregisteredGet() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                registry.get("NotRegisteredComponent"));
    }
}
