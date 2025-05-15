package com.crina.djoor.shared.config;

import org.springframework.beans.factory.annotation.Value;

public abstract class BaseConfig {
    @Value("${app.env:dev}")
    protected String env;
}
