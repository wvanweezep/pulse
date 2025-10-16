package com.wvanw.pulse.commons;

import com.wvanw.pulse.components.Component;
import com.wvanw.pulse.core.IParsable;

import java.util.Objects;

public class ParsableComponent implements Component, IParsable {

    private int i;

    public ParsableComponent(int i) {
        this.i = i;
    }

    public static Component parse(String value) {
        return new ParsableComponent(Integer.parseInt(value));
    }

    @Override public void update() {}
    @Override public void fixedUpdate() {}
    @Override public void copy() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsableComponent that = (ParsableComponent) o;
        return i == that.i;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(i);
    }
}
