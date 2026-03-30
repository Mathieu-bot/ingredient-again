package com.hei.prog3.ingredientagain.repository;

import com.hei.prog3.ingredientagain.entity.Dish;

import java.util.List;

public interface DishRepository {

    List<Dish> findAll();

    List<Dish> findAllWithIngredients();

    void updateDishIngredients(int dishId, List<Integer> ingredientIds);
}