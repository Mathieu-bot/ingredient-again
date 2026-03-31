package com.hei.prog3.ingredientagain.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseResetTest {

    private DataSourceConfig dataSourceConfig;

    @BeforeEach
    void init() {
        dataSourceConfig = new DataSourceConfig();
    }

    @BeforeEach
    void resetDatabaseTables() throws SQLException {
        try (Connection conn = dataSourceConfig.getDBConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("SET CONSTRAINTS ALL DEFERRED;");

            stmt.execute("DELETE FROM dish_ingredient;");
            stmt.execute("DELETE FROM stock_movement;");

            stmt.execute("DELETE FROM ingredient;");
            stmt.execute("DELETE FROM dish;");

            stmt.execute("SELECT setval(pg_get_serial_sequence('dish', 'id'), 1, false);");
            stmt.execute("SELECT setval(pg_get_serial_sequence('ingredient', 'id'), 1, false);");
            stmt.execute("SELECT setval(pg_get_serial_sequence('dish_ingredient', 'id'), 1, false);");
            stmt.execute("SELECT setval(pg_get_serial_sequence('stock_movement', 'id'), 1, false);");
        }
    }

    @Test
    void testDatabaseIsReset() throws SQLException {
        try (Connection conn = dataSourceConfig.getDBConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet dishRs = stmt.executeQuery("SELECT COUNT(id) FROM dish;");
            assertTrue(dishRs.next());
            assertEquals(0, dishRs.getInt(1));

            ResultSet ingredientRs = stmt.executeQuery("SELECT COUNT(id) FROM ingredient;");
            assertTrue(ingredientRs.next());
            assertEquals(0, ingredientRs.getInt(1));

            ResultSet dishIngredientRs = stmt.executeQuery("SELECT COUNT(id) FROM dish_ingredient;");
            assertTrue(dishIngredientRs.next());
            assertEquals(0, dishIngredientRs.getInt(1));

            ResultSet stockMovementRs = stmt.executeQuery("SELECT COUNT(id) FROM stock_movement;");
            assertTrue(stockMovementRs.next());
            assertEquals(0, stockMovementRs.getInt(1));
        }
    }

    @Test
    void testSequencesAreReset() throws SQLException {
        try (Connection conn = dataSourceConfig.getDBConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("INSERT INTO dish (name, dish_type) VALUES ('Test Dish', 'START');");

            ResultSet rs = stmt.executeQuery("SELECT id FROM dish WHERE name = 'Test Dish';");
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("id"));
        }
    }

    @Test
    void testCanInsertSampleData() throws SQLException {
        try (Connection conn = dataSourceConfig.getDBConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("INSERT INTO ingredient (name, price, category) VALUES " +
                    "('Tomate', 100.0, 'VEGETABLE'), " +
                    "('Laitue', 50.0, 'VEGETABLE');");

            stmt.execute("INSERT INTO dish (name, dish_type) VALUES " +
                    "('Salade', 'START');");

            stmt.execute("INSERT INTO dish_ingredient (id_dish, id_ingredient, quantity_required, unit) VALUES " +
                    "(1, 1, 0.15, 'KG'), " +
                    "(1, 2, 0.20, 'KG');");

            ResultSet ingredientRs = stmt.executeQuery("SELECT COUNT(id) FROM ingredient;");
            assertTrue(ingredientRs.next());
            assertEquals(2, ingredientRs.getInt(1));

            ResultSet dishRs = stmt.executeQuery("SELECT COUNT(id) FROM dish;");
            assertTrue(dishRs.next());
            assertEquals(1, dishRs.getInt(1));

            ResultSet dishIngredientRs = stmt.executeQuery("SELECT COUNT(id) FROM dish_ingredient;");
            assertTrue(dishIngredientRs.next());
            assertEquals(2, dishIngredientRs.getInt(1));
        }
    }
}
