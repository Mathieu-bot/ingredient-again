package com.hei.prog3.ingredientagain.service;

import com.hei.prog3.ingredientagain.dto.DishDTO;
import com.hei.prog3.ingredientagain.entity.Dish;
import com.hei.prog3.ingredientagain.exception.DishNotFoundException;
import com.hei.prog3.ingredientagain.mapper.DishMapper;
import com.hei.prog3.ingredientagain.repository.DishRepository;

import java.util.List;

public class DishService {

    private final DishRepository repository;

    public DishService(DishRepository repository) {
        this.repository = repository;
    }

    public List<DishDTO> findAll() {
        List<Dish> dishes = repository.findAllWithIngredients();
        return DishMapper.toDTO(dishes);
    }

    public void updateDishIngredients(int dishId, List<Integer> ingredientIds) {
        try {
            repository.updateDishIngredients(dishId, ingredientIds);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Dish not found")) {
                throw new DishNotFoundException(dishId);
            }
            throw e;
        }
    }
}