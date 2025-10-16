package com.wvanw.pulse.commons;

import com.wvanw.pulse.components.Component;

import java.util.Objects;

public class TestComponent implements Component {

    public int fieldInt;
    public Double fieldDouble;
    public String fieldString;
    public ParsableComponent fieldParsable;

    public TestComponent() {}

    public TestComponent(int fieldInt, double fieldDouble, String fieldString, ParsableComponent fieldParsable) {
        this.fieldInt = fieldInt;
        this.fieldDouble = fieldDouble;
        this.fieldString = fieldString;
        this.fieldParsable = fieldParsable;
    }


    @Override public void update() {}
    @Override public void fixedUpdate() {}
    @Override public void copy() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestComponent that = (TestComponent) o;
        return fieldInt == that.fieldInt && Objects.equals(fieldDouble, that.fieldDouble) && Objects.equals(fieldString, that.fieldString) && Objects.equals(fieldParsable, that.fieldParsable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldInt, fieldDouble, fieldString, fieldParsable);
    }
}