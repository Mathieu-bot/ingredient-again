package com.hei.prog3.ingredientagain.controller;

import com.hei.prog3.ingredientagain.dto.CreateStockMovementRequest;
import com.hei.prog3.ingredientagain.dto.StockMovementDTO;
import com.hei.prog3.ingredientagain.entity.Ingredient;
import com.hei.prog3.ingredientagain.entity.StockMovement;
import com.hei.prog3.ingredientagain.entity.StockValue;
import com.hei.prog3.ingredientagain.entity.Unit;
import com.hei.prog3.ingredientagain.exception.MissingParameterException;
import com.hei.prog3.ingredientagain.mapper.StockMovementMapper;
import com.hei.prog3.ingredientagain.service.IngredientService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService service;
    private final StockMovementMapper stockMovementMapper;

    public IngredientController(IngredientService service, StockMovementMapper stockMovementMapper) {
        this.service = service;
        this.stockMovementMapper = stockMovementMapper;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll() {
        List<Ingredient> ingredients = service.findAll();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getById(@PathVariable int id) {
        Ingredient ingredient = service.findById(id);
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<StockValue> getStockValue(
            @PathVariable int id,
            @RequestParam(value = "at", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant at,
            @RequestParam(value = "unit", required = false) Unit unit) {

        if (at == null || unit == null) {
            throw new MissingParameterException();
        }

        StockValue stockValue = service.getStockValueAt(id, at, unit);
        return ResponseEntity.ok(stockValue);
    }

    @GetMapping("/{id}/stockMovements")
    public ResponseEntity<List<StockMovementDTO>> getStockMovements(
            @PathVariable int id,
            @RequestParam(value = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(value = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {

        List<StockMovement> movements = service.getStockMovementsByDateRange(id, from, to);
        List<StockMovementDTO> movementDTOs = stockMovementMapper.toDTOList(movements);
        return ResponseEntity.ok(movementDTOs);
    }

    @PostMapping("/{id}/stockMovements")
    public ResponseEntity<List<StockMovementDTO>> createStockMovements(
            @PathVariable int id,
            @RequestBody List<CreateStockMovementRequest> requests) {

        List<StockMovement> createdMovements = service.createStockMovements(id, requests);
        List<StockMovementDTO> movementDTOs = stockMovementMapper.toDTOList(createdMovements);
        return ResponseEntity.ok(movementDTOs);
    }
}