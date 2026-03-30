package com.hei.prog3.ingredientagain.service;

import com.hei.prog3.ingredientagain.entity.Dish;
import com.hei.prog3.ingredientagain.repository.DishRepository;

import java.util.List;

public class DishService {

    private final DishRepository repository;

    public DishService(DishRepository repository) {
        this.repository = repository;
    }

    public List<Dish> findAll() {
        return repository.findAllWithIngredients();
    }
}