package com.hei.prog3.ingredientagain.dto;

import com.hei.prog3.ingredientagain.entity.MovementTypeEnum;
import com.hei.prog3.ingredientagain.entity.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementDTO {
    private int id;
    private Instant creationDateTime;
    private Unit unit;
    private double quantity;
    private MovementTypeEnum type;
}