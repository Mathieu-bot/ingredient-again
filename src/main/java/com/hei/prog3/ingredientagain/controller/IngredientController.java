package com.hei.prog3.ingredientagain.controller;

import com.hei.prog3.ingredientagain.config.DataSource;
import com.hei.prog3.ingredientagain.entity.Ingredient;
import com.hei.prog3.ingredientagain.repository.impl.JdbcIngredientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final JdbcIngredientRepository repository;

    public IngredientController() {
        DataSource dataSource = new DataSource();
        this.repository = new JdbcIngredientRepository(dataSource);
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll() {
        List<Ingredient> ingredients = repository.findAll();
        return ResponseEntity.ok(ingredients);
    }
}