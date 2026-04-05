package io.cred0.core.clients.config;

import java.util.List;

import org.springframework.security.jackson.SecurityJacksonModules;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.json.JsonMapper;

public final class RegisteredClientRepositoryConfig {

    private RegisteredClientRepositoryConfig() {
    }

    public static JsonMapper registeredClientJsonMapper() {
        List<JacksonModule> modules = SecurityJacksonModules.getModules(RegisteredClientRepositoryConfig.class.getClassLoader());
        return JsonMapper.builder().addModules(modules).build();
    }

}
