package com.wvanw.pulse.datalang.builders;

import com.wvanw.pulse.commons.TestComponent;
import com.wvanw.pulse.components.ComponentRegistry;
import com.wvanw.pulse.datalang.dto.ParsedObject;
import com.wvanw.pulse.prefabs.Prefab;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PrefabBuilderTest {

    @BeforeAll
    public static void setUp() {
        ComponentRegistry.getInstance().register("TestComponent", TestComponent.class);
    }

    @Test
    public void buildsAllBuiltInFieldTypes() {
        ParsedObject obj = new ParsedObject("Prefab", Map.of(
                "TestComponent", Map.of(
                        "strField", "str",
                        "boolField", "true",
                        "intField", "1",
                        "floatField","2.f",
                        "doubleField", "3.d",
                        "longField", "4"
                )
        ));

        Prefab prefab = new PrefabBuilder(obj, ComponentRegistry.getInstance()).build();
        assertThat(prefab.getName()).isEqualTo("Prefab");
        assertThat(prefab.getComponents()).hasSize(1);
        assertThat(prefab.getComponents().getFirst()).usingRecursiveAssertion().isEqualTo(
                new TestComponent("str", true, 1, 2, 3, 4, null));
    }

    @Test
    public void buildsSingleNestedObject() {
        ParsedObject obj = new ParsedObject("Prefab", Map.of(
                "TestComponent", Map.of(
                        "strField", "str",
                        "boolField", "true",
                        "intField", "1",
                        "floatField","2.f",
                        "doubleField", "3.d",
                        "longField", "4",
                        "componentField", Map.of(
                                "strField", "str",
                                "boolField", "false",
                                "intField", "1",
                                "floatField","2.f",
                                "doubleField", "3.d",
                                "longField", "4"
                        )
                )
        ));

        Prefab prefab = new PrefabBuilder(obj, ComponentRegistry.getInstance()).build();
        assertThat(prefab.getName()).isEqualTo("Prefab");
        assertThat(prefab.getComponents()).hasSize(1);
        assertThat(prefab.getComponents().getFirst()).usingRecursiveAssertion().isEqualTo(
                new TestComponent("str", true, 1, 2, 3, 4,
                        new TestComponent("str", false, 1, 2, 3, 4, null)));
    }

}
