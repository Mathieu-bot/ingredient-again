package com.hei.prog3.ingredientagain.mapper;

import com.hei.prog3.ingredientagain.dto.StockMovementDTO;
import com.hei.prog3.ingredientagain.entity.StockMovement;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockMovementMapper {

    public StockMovementDTO toDTO(StockMovement movement) {
        return new StockMovementDTO(
                movement.getId(),
                movement.getCreationDateTime(),
                movement.getValue().getUnit(),
                movement.getValue().getQuantity(),
                movement.getType()
        );
    }

    public List<StockMovementDTO> toDTOList(List<StockMovement> movements) {
        return movements.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}