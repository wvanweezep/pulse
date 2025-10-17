package com.wvanw.pulse.datalang.dto;

import java.util.Map;

public record ParsedObject(String name, Map<String, Object> objects) {}
