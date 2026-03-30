package com.hei.prog3.ingredientagain.controller;

import com.hei.prog3.ingredientagain.dto.DishDTO;
import com.hei.prog3.ingredientagain.dto.IngredientReferenceDTO;
import com.hei.prog3.ingredientagain.dto.UpdateDishIngredientsRequest;
import com.hei.prog3.ingredientagain.exception.MissingRequestBodyException;
import com.hei.prog3.ingredientagain.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService service;

    public DishController(DishService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DishDTO>> getAll() {
        List<DishDTO> dishes = service.findAll();
        return ResponseEntity.ok(dishes);
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<String> updateIngredients(
            @PathVariable int id,
            @RequestBody UpdateDishIngredientsRequest request) {

        if (request == null || request.getIngredients() == null) {
            throw new MissingRequestBodyException();
        }

        List<Integer> ingredientIds = request.getIngredients().stream()
                .map(IngredientReferenceDTO::getId)
                .collect(Collectors.toList());

        service.updateDishIngredients(id, ingredientIds);

        return ResponseEntity.ok().body("Ingredients updated successfully");
    }
}