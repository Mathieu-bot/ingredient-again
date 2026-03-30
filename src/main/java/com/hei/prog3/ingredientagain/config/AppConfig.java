package com.hei.prog3.ingredientagain.config;

import com.hei.prog3.ingredientagain.repository.DishRepository;
import com.hei.prog3.ingredientagain.repository.IngredientRepository;
import com.hei.prog3.ingredientagain.repository.impl.JdbcDishRepository;
import com.hei.prog3.ingredientagain.repository.impl.JdbcIngredientRepository;
import com.hei.prog3.ingredientagain.service.DishService;
import com.hei.prog3.ingredientagain.service.IngredientService;
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

    @Bean
    public IngredientService ingredientService(IngredientRepository repository) {
        return new IngredientService(repository);
    }

    @Bean
    public DishRepository dishRepository(DataSourceConfig dataSourceConfig) {
        return new JdbcDishRepository(dataSourceConfig);
    }

    @Bean
    public DishService dishService(DishRepository repository) {
        return new DishService(repository);
    }
}