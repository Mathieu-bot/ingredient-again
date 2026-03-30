package com.hei.prog3.ingredientagain.dto;

import com.hei.prog3.ingredientagain.entity.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientDTO {
    private int id;
    private String name;
    private CategoryEnum category;
    private double price;
}