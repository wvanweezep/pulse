package com.wvanw.pulse.core;

import com.wvanw.pulse.components.Component;

public interface IParsable {

    static Component parse(String value) {
        throw new RuntimeException("No implementation provided");
    }
}
