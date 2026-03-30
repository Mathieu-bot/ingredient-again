package com.hei.prog3.ingredientagain.mapper;

import com.hei.prog3.ingredientagain.dto.DishDTO;
import com.hei.prog3.ingredientagain.dto.IngredientDTO;
import com.hei.prog3.ingredientagain.entity.Dish;
import com.hei.prog3.ingredientagain.entity.DishIngredient;
import com.hei.prog3.ingredientagain.entity.Ingredient;

import java.util.List;
import java.util.stream.Collectors;

public class DishMapper {

    public static DishDTO toDTO(Dish dish) {
        List<IngredientDTO> ingredientDTOs = dish.getIngredients().stream()
                .map(DishMapper::toDTO)
                .collect(Collectors.toList());

        return new DishDTO(
                dish.getId(),
                dish.getName(),
                dish.getDishType(),
                dish.getPrice(),
                ingredientDTOs
        );
    }

    public static List<DishDTO> toDTO(List<Dish> dishes) {
        return dishes.stream()
                .map(DishMapper::toDTO)
                .collect(Collectors.toList());
    }

    private static IngredientDTO toDTO(Ingredient ingredient) {
        return new IngredientDTO(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getCategory(),
                ingredient.getPrice()
        );
    }
}