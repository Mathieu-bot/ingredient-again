package com.hei.prog3.ingredientagain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DishIngredient {
    private Dish dish;
    private Ingredient ingredient;
    private double quantity;
    private Unit unit;
}