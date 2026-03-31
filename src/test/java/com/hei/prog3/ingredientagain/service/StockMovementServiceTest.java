package com.hei.prog3.ingredientagain.service;

import com.hei.prog3.ingredientagain.config.DataSourceConfig;
import com.hei.prog3.ingredientagain.dto.CreateStockMovementRequest;
import com.hei.prog3.ingredientagain.entity.MovementTypeEnum;
import com.hei.prog3.ingredientagain.entity.StockMovement;
import com.hei.prog3.ingredientagain.entity.Unit;
import com.hei.prog3.ingredientagain.repository.impl.JdbcIngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockMovementServiceTest {

    private IngredientService service;
    private DataSourceConfig dataSourceConfig;

    @BeforeEach
    void init() {
        dataSourceConfig = new DataSourceConfig();
        JdbcIngredientRepository repository = new JdbcIngredientRepository(dataSourceConfig);
        service = new IngredientService(repository);
    }

    @BeforeEach
    void resetDatabaseTables() throws Exception {
        try (var conn = dataSourceConfig.getDBConnection();
             var stmt = conn.createStatement()) {

            stmt.execute("SET CONSTRAINTS ALL DEFERRED;");
            stmt.execute("DELETE FROM dish_ingredient;");
            stmt.execute("DELETE FROM stock_movement;");
            stmt.execute("DELETE FROM ingredient;");
            stmt.execute("DELETE FROM dish;");
            stmt.execute("SELECT setval(pg_get_serial_sequence('dish', 'id'), 1, false);");
            stmt.execute("SELECT setval(pg_get_serial_sequence('ingredient', 'id'), 1, false);");
            stmt.execute("SELECT setval(pg_get_serial_sequence('dish_ingredient', 'id'), 1, false);");
            stmt.execute("SELECT setval(pg_get_serial_sequence('stock_movement', 'id'), 1, false);");

            // Insérer un ingrédient de test
            stmt.execute("INSERT INTO ingredient (name, price, category) VALUES " +
                    "('Tomate', 100.0, 'VEGETABLE');");
        }
    }

    @Test
    void testGetStockMovementsByDateRange_returnsMovementsInDateRange() {
        // Given
        Instant from = Instant.parse("2024-01-01T00:00:00Z");
        Instant to = Instant.parse("2024-12-31T23:59:59Z");

        // Insert test data
        try (var conn = dataSourceConfig.getDBConnection();
             var stmt = conn.createStatement()) {

            stmt.execute("INSERT INTO stock_movement (id_ingredient, quantity, type, unit, creation_datetime) VALUES " +
                    "(1, 5.0, 'IN', 'KG', '2024-06-15 10:00:00'), " +
                    "(1, 2.0, 'OUT', 'KG', '2024-06-20 14:30:00'), " +
                    "(1, 3.0, 'IN', 'KG', '2024-07-01 09:00:00'), " +
                    "(1, 1.0, 'OUT', 'KG', '2023-12-31 23:59:59')"); // Hors plage
        } catch (Exception e) {
            fail("Failed to setup test data: " + e.getMessage());
        }

        // When
        List<StockMovement> movements = service.getStockMovementsByDateRange(1, from, to);

        // Then
        assertNotNull(movements);
        assertEquals(3, movements.size());

        StockMovement firstMovement = movements.get(0);
        assertEquals(1, firstMovement.getId());
        assertEquals(Unit.KG, firstMovement.getValue().getUnit());
        assertEquals(5.0, firstMovement.getValue().getQuantity(), 0.001);
        assertEquals(MovementTypeEnum.IN, firstMovement.getType());
    }

    @Test
    void testGetStockMovementsByDateRange_ingredientNotFound_throwsException() {
        // Given
        Instant from = Instant.parse("2024-01-01T00:00:00Z");
        Instant to = Instant.parse("2024-12-31T23:59:59Z");

        // When & Then
        assertThrows(com.hei.prog3.ingredientagain.exception.IngredientNotFoundException.class,
                () -> service.getStockMovementsByDateRange(999, from, to));
    }

    @Test
    void testCreateStockMovements_createsAndReturnsMovements() {
        // Given
        List<CreateStockMovementRequest> requests = List.of(
                new CreateStockMovementRequest(Unit.KG, 10.0, MovementTypeEnum.IN),
                new CreateStockMovementRequest(Unit.PCS, 5.0, MovementTypeEnum.OUT)
        );

        // When
        List<StockMovement> createdMovements = service.createStockMovements(1, requests);

        // Then
        assertNotNull(createdMovements);
        assertEquals(2, createdMovements.size());

        StockMovement firstMovement = createdMovements.get(0);
        assertTrue(firstMovement.getId() > 0);
        assertEquals(Unit.KG, firstMovement.getValue().getUnit());
        assertEquals(10.0, firstMovement.getValue().getQuantity(), 0.001);
        assertEquals(MovementTypeEnum.IN, firstMovement.getType());
        assertNotNull(firstMovement.getCreationDateTime());

        StockMovement secondMovement = createdMovements.get(1);
        assertTrue(secondMovement.getId() > 0);
        assertEquals(Unit.PCS, secondMovement.getValue().getUnit());
        assertEquals(5.0, secondMovement.getValue().getQuantity(), 0.001);
        assertEquals(MovementTypeEnum.OUT, secondMovement.getType());
        assertNotNull(secondMovement.getCreationDateTime());

        // Verify in database
        try (var conn = dataSourceConfig.getDBConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(id) FROM stock_movement WHERE id_ingredient = 1")) {

            assertTrue(rs.next());
            assertEquals(2, rs.getInt(1));
        } catch (Exception e) {
            fail("Failed to verify database: " + e.getMessage());
        }
    }

    @Test
    void testCreateStockMovements_ingredientNotFound_throwsException() {
        // Given
        List<CreateStockMovementRequest> requests = List.of(
                new CreateStockMovementRequest(Unit.KG, 10.0, MovementTypeEnum.IN)
        );

        // When & Then
        assertThrows(com.hei.prog3.ingredientagain.exception.IngredientNotFoundException.class,
                () -> service.createStockMovements(999, requests));
    }
}