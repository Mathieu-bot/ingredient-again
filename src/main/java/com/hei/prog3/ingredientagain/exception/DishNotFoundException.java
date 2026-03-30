package com.hei.prog3.ingredientagain.exception;

public class DishNotFoundException extends RuntimeException {

    public DishNotFoundException(int id) {
        super("Dish.id=" + id + " is not found");
    }
}