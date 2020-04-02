package com.softserveinc.webapp;

import com.softserveinc.webapp.model.User;
import module1.task2.SSHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Collections;
import java.util.Map;

@Configuration
public class StorageConfig {

    @Bean
    public Map<Long, User> userStorage() {
        return Collections.synchronizedMap(new SSHashMap<>());
    }

    @Bean
    public Validator validator(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

}
