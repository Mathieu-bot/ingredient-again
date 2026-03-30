package com.hei.prog3.ingredientagain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockValue {
    private double quantity;
    private Unit unit;
}