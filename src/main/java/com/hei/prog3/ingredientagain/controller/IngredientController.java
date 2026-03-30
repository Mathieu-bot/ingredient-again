package com.hei.prog3.ingredientagain.controller;

import com.hei.prog3.ingredientagain.entity.Ingredient;
import com.hei.prog3.ingredientagain.entity.StockValue;
import com.hei.prog3.ingredientagain.entity.Unit;
import com.hei.prog3.ingredientagain.service.IngredientService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService service;

    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll() {
        List<Ingredient> ingredients = service.findAll();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getById(@PathVariable int id) {
        Ingredient ingredient = service.findById(id);
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<StockValue> getStockValue(
            @PathVariable int id,
            @RequestParam("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant at,
            @RequestParam("unit") Unit unit) {

        StockValue stockValue = service.getStockValueAt(id, at, unit);
        return ResponseEntity.ok(stockValue);
    }
}