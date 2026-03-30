package com.hei.prog3.ingredientagain.config;

import com.hei.prog3.ingredientagain.repository.IngredientRepository;
import com.hei.prog3.ingredientagain.repository.impl.JdbcIngredientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DataSourceConfig dataSourceConfig() {
        return new DataSourceConfig();
    }

    @Bean
    public IngredientRepository ingredientRepository(DataSourceConfig dataSourceConfig) {
        return new JdbcIngredientRepository(dataSourceConfig);
    }
}