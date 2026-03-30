package com.hei.prog3.ingredientagain.entity;

import com.hei.prog3.ingredientagain.service.UnitConversionService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class Ingredient {
    private int id;
    private String name;
    private double price;
    private CategoryEnum category;
    private Double quantity;
    private List<StockMovement> stockMovementList;

    public StockValue getStockValueAt(Instant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("instant must not be null");
        }

        if (stockMovementList == null || stockMovementList.isEmpty()) {
            return new StockValue(0.0, Unit.KG);
        }

        double totalQuantityInKg = 0.0;

        for (StockMovement movement : stockMovementList) {
            if (movement == null) {
                continue;
            }

            Instant movementTime = movement.getCreationDateTime();
            if (movementTime == null || movementTime.isAfter(instant)) {
                continue;
            }

            StockValue value = movement.getValue();
            if (value == null) {
                continue;
            }

            Unit movementUnit = value.getUnit() == null ? Unit.KG : value.getUnit();
            double quantityInKg = UnitConversionService.convert(
                    id, value.getQuantity(), movementUnit, Unit.KG);

            if (movement.getType() == MovementTypeEnum.IN) {
                totalQuantityInKg += quantityInKg;
            } else if (movement.getType() == MovementTypeEnum.OUT) {
                totalQuantityInKg -= quantityInKg;
            }
        }

        return new StockValue(totalQuantityInKg, Unit.KG);
    }
}