package com.hei.prog3.ingredientagain.exception;

public class IngredientNotFoundException extends RuntimeException {

    public IngredientNotFoundException(int id) {
        super("Ingredient.id=" + id + " is not found");
    }

    public IngredientNotFoundException(String message) {
        super(message);
    }
}