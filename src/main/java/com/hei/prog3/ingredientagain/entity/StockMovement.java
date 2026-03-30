package com.hei.prog3.ingredientagain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class StockMovement {
    private int id;
    private StockValue value;
    private MovementTypeEnum type;
    private Instant creationDateTime;
}