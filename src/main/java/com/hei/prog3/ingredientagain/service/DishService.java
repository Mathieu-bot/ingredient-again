package com.hei.prog3.ingredientagain.service;

import com.hei.prog3.ingredientagain.dto.DishDTO;
import com.hei.prog3.ingredientagain.entity.Dish;
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
}