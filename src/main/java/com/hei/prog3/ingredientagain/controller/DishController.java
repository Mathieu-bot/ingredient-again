package com.hei.prog3.ingredientagain.controller;

import com.hei.prog3.ingredientagain.dto.DishDTO;
import com.hei.prog3.ingredientagain.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}