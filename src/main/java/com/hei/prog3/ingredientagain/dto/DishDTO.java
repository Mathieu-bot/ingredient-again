package com.hei.prog3.ingredientagain.dto;

import com.hei.prog3.ingredientagain.entity.DishTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DishDTO {
    private int id;
    private String name;
    private DishTypeEnum dishType;
    private Double price;
    private List<IngredientDTO> ingredients;
}