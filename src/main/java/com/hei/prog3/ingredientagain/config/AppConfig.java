package com.hei.prog3.ingredientagain.config;

import com.hei.prog3.ingredientagain.repository.IngredientRepository;
import com.hei.prog3.ingredientagain.repository.impl.JdbcIngredientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new DataSource();
    }

    @Bean
    public IngredientRepository ingredientRepository(DataSource dataSource) {
        return new JdbcIngredientRepository(dataSource);
    }
}