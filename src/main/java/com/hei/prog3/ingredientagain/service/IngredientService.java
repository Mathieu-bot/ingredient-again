package com.hei.prog3.ingredientagain.service;

import com.hei.prog3.ingredientagain.dto.CreateStockMovementRequest;
import com.hei.prog3.ingredientagain.entity.Ingredient;
import com.hei.prog3.ingredientagain.entity.StockMovement;
import com.hei.prog3.ingredientagain.entity.StockValue;
import com.hei.prog3.ingredientagain.entity.Unit;
import com.hei.prog3.ingredientagain.repository.IngredientRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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

        double quantityInRequestedUnit = UnitConversionService.convert(
                ingredientId,
                stockValueInKg.getQuantity(),
                Unit.KG,
                unit
        );

        return new StockValue(quantityInRequestedUnit, unit);
    }

    public List<StockMovement> getStockMovementsByDateRange(int ingredientId, Instant from, Instant to) {
        repository.findById(ingredientId);
        return repository.findStockMovementsByIngredientIdAndDateRange(ingredientId, from, to);
    }

    public List<StockMovement> createStockMovements(int ingredientId, List<CreateStockMovementRequest> requests) {
        repository.findById(ingredientId);

        Instant now = Instant.now();

        List<StockMovement> movements = requests.stream()
                .map(req -> {
                    StockValue value = new StockValue(req.getQuantity(), req.getUnit());
                    return new StockMovement(0, value, req.getType(), now);
                })
                .collect(Collectors.toList());

        return repository.createStockMovements(ingredientId, movements);
    }
}