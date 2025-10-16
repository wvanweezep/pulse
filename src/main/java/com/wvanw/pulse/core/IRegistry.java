package com.wvanw.pulse.core;

public interface IRegistry<T> {

    void register(String name, T object);

    T get(String name);

    int size();

    void clear();
}
