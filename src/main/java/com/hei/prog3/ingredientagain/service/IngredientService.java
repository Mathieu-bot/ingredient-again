package com.hei.prog3.ingredientagain.service;

import com.hei.prog3.ingredientagain.entity.Ingredient;
import com.hei.prog3.ingredientagain.entity.StockMovement;
import com.hei.prog3.ingredientagain.entity.StockValue;
import com.hei.prog3.ingredientagain.entity.Unit;
import com.hei.prog3.ingredientagain.repository.IngredientRepository;

import java.time.Instant;
import java.util.List;

public class IngredientService {

    private final IngredientRepository repository;

    public IngredientService(IngredientRepository repository) {
        this.repository = repository;
    }

    public List<Ingredient> findAll() {
        return repository.findAll();
    }

    public Ingredient findById(int id) {
        return repository.findById(id);
    }

    public StockValue getStockValueAt(int ingredientId, Instant at, Unit unit) {
        Ingredient ingredient = repository.findByIdWithMovements(ingredientId);

        StockValue stockValueInKg = ingredient.getStockValueAt(at);

        if (unit == Unit.KG) {
            return stockValueInKg;
        }

        // Convertir vers l'unité demandée
        double quantityInRequestedUnit = UnitConversionService.convert(
                ingredientId,
                stockValueInKg.getQuantity(),
                Unit.KG,
                unit
        );

        return new StockValue(quantityInRequestedUnit, unit);
    }
}