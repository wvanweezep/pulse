package com.wvanw.pulse.commons;

import com.wvanw.pulse.components.Component;

import java.util.Objects;

public class TestComponent implements Component {

    private String strField;
    private boolean boolField;
    private int intField;
    private float floatField;
    private double doubleField;
    private long longField;
    private TestComponent componentField;

    public TestComponent() {}

    public TestComponent(String strField, boolean boolField, int intField, float floatField, double doubleField, long longField, TestComponent componentField) {
        this.strField = strField;
        this.boolField = boolField;
        this.intField = intField;
        this.floatField = floatField;
        this.doubleField = doubleField;
        this.longField = longField;
        this.componentField = componentField;
    }

    public String getStrField() {
        return strField;
    }

    public boolean isBoolField() {
        return boolField;
    }

    public int getIntField() {
        return intField;
    }

    public float getFloatField() {
        return floatField;
    }

    public double getDoubleField() {
        return doubleField;
    }

    public long getLongField() {
        return longField;
    }

    public TestComponent getComponentField() {
        return componentField;
    }

    @Override public void update() {}
    @Override public void fixedUpdate() {}
    @Override public void copy() {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TestComponent that = (TestComponent) o;
        return boolField == that.boolField && intField == that.intField && Float.compare(floatField, that.floatField) == 0 && Double.compare(doubleField, that.doubleField) == 0 && longField == that.longField && Objects.equals(strField, that.strField) && Objects.equals(componentField, that.componentField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strField, boolField, intField, floatField, doubleField, longField, componentField);
    }

    @Override
    public String toString() {
        return "TestComponent{" +
                "strField='" + strField + '\'' +
                ", boolField=" + boolField +
                ", intField=" + intField +
                ", floatField=" + floatField +
                ", doubleField=" + doubleField +
                ", longField=" + longField +
                ", componentField=" + componentField +
                '}';
    }
}
