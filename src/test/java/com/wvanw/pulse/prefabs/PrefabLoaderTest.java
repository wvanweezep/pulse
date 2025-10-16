package com.wvanw.pulse.prefabs;

import com.wvanw.pulse.commons.ParsableComponent;
import com.wvanw.pulse.commons.TestComponent;
import com.wvanw.pulse.components.ComponentRegistry;
import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;

public class PrefabLoaderTest {

    @BeforeAll
    public static void setUp() {
        ComponentRegistry.getInstance().register("TestComponent", TestComponent.class);
    }

    @Test
    public void testParseComponent() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String line = "TestComponent: fieldInt=2, fieldDouble=3.5, fieldString=Test, fieldParsable=2";
        TestComponent result = (TestComponent) PrefabLoader.parseComponent(line, 0);
        Assertions.assertEquals(new TestComponent(2, 3.5, "Test", new ParsableComponent(2)), result);
    }

    @Test
    public void testParseValue() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Assertions.assertEquals(new ParsableComponent(0),
                PrefabLoader.parseValue(ParsableComponent.class, "0"));
    }
}
