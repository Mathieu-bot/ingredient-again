package com.hei.prog3.ingredientagain.repository;

import com.hei.prog3.ingredientagain.entity.Ingredient;

import java.util.List;

public interface IngredientRepository {

    List<Ingredient> findAll();
    Ingredient findById(int id);
}