package com.hei.prog3.ingredientagain.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateDishIngredientsRequest {
    private List<IngredientReferenceDTO> ingredients;
}