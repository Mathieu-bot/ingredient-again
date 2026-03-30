package com.hei.prog3.ingredientagain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Dish {
    private int id;
    private String name;
    private DishTypeEnum dishType;
    private Double price;
    private List<DishIngredient> dishIngredients;

    public Dish(int id, String name, DishTypeEnum dishType, Double price) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.price = price;
        this.dishIngredients = new ArrayList<>();
    }

    public Dish(int id, String name, DishTypeEnum dishType) {
        this(id, name, dishType, null);
    }

    public List<Ingredient> getIngredients() {
        List<Ingredient> result = new ArrayList<>();
        for (DishIngredient di : dishIngredients) {
            Ingredient ingredient = di.getIngredient();
            ingredient.setQuantity(di.getQuantity());
            result.add(ingredient);
        }
        return result;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.dishIngredients.clear();
        if (ingredients == null) {
            return;
        }
        for (Ingredient ingredient : ingredients) {
            if (ingredient == null) {
                continue;
            }
            double quantity = ingredient.getQuantity() == null ? 1.0 : ingredient.getQuantity();
            this.dishIngredients.add(new DishIngredient(this, ingredient, quantity, Unit.KG));
        }
    }

    public void addIngredient(Ingredient ingredient) {
        if (ingredient != null) {
            double quantity = ingredient.getQuantity() == null ? 1.0 : ingredient.getQuantity();
            this.dishIngredients.add(new DishIngredient(this, ingredient, quantity, Unit.KG));
        }
    }

    public Double getDishCost() {
        double totalPrice = 0;
        for (DishIngredient di : dishIngredients) {
            totalPrice += di.getIngredient().getPrice() * di.getQuantity();
        }
        return totalPrice;
    }

    public Double getGrossMargin() {
        if (price == null) {
            throw new IllegalStateException("Price not found, not possible to calculate margin.");
        }
        return price - getDishCost();
    }
}