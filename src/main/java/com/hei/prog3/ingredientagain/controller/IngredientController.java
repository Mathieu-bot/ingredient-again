package com.hei.prog3.ingredientagain.controller;

import com.hei.prog3.ingredientagain.entity.Ingredient;
import com.hei.prog3.ingredientagain.repository.IngredientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientRepository repository;

    public IngredientController(IngredientRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll() {
        List<Ingredient> ingredients = repository.findAll();
        return ResponseEntity.ok(ingredients);
    }
}