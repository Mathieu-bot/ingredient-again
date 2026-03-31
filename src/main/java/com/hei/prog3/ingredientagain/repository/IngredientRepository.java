package com.hei.prog3.ingredientagain.repository;

import com.hei.prog3.ingredientagain.entity.Ingredient;
import com.hei.prog3.ingredientagain.entity.StockMovement;

import java.time.Instant;
import java.util.List;

public interface IngredientRepository {

    List<Ingredient> findAll();
    Ingredient findById(int id);
    Ingredient findByIdWithMovements(int id);
    List<StockMovement> findStockMovementsByIngredientIdAndDateRange(int ingredientId, Instant from, Instant to);
    List<StockMovement> createStockMovements(int ingredientId, List<StockMovement> movements);
}