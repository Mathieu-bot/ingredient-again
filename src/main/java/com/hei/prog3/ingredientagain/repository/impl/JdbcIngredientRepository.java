package com.hei.prog3.ingredientagain.repository.impl;

import com.hei.prog3.ingredientagain.config.DataSourceConfig;
import com.hei.prog3.ingredientagain.entity.*;
import com.hei.prog3.ingredientagain.exception.IngredientNotFoundException;
import com.hei.prog3.ingredientagain.repository.IngredientRepository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcIngredientRepository implements IngredientRepository {

    private final DataSourceConfig dataSource;

    public JdbcIngredientRepository(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Ingredient> findAll() {
        String sql = "SELECT id, name, category, price FROM ingredient";

        List<Ingredient> ingredients = new ArrayList<>();
        Connection conn = dataSource.getDBConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ingredients.add(mapIngredient(rs));
            }

            return ingredients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.close(conn);
        }
    }

    @Override
    public Ingredient findById(int id) {
        String sql = "SELECT id, name, category, price FROM ingredient WHERE id = ?";

        Connection conn = dataSource.getDBConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapIngredient(rs);
            }

            throw new IngredientNotFoundException(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.close(conn);
        }
    }

    private Ingredient mapIngredient(ResultSet rs) throws SQLException {
        return new Ingredient(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                CategoryEnum.valueOf(rs.getString("category").toUpperCase()),
                null,
                null
        );
    }

    @Override
    public Ingredient findByIdWithMovements(int id) {
        String sql = "SELECT id, name, category, price FROM ingredient WHERE id = ?";

        Connection conn = dataSource.getDBConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Ingredient ingredient = mapIngredient(rs);

                List<StockMovement> movements = findStockMovementsByIngredientId(conn, id);
                ingredient.setStockMovementList(movements);

                return ingredient;
            }

            throw new IngredientNotFoundException(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.close(conn);
        }
    }

    private List<StockMovement> findStockMovementsByIngredientId(Connection conn, int ingredientId) throws SQLException {
        String sql = "SELECT id, quantity, type, unit, creation_datetime FROM stock_movement WHERE id_ingredient = ?";

        List<StockMovement> movements = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, ingredientId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            StockValue value = new StockValue(
                    rs.getDouble("quantity"),
                    Unit.valueOf(rs.getString("unit"))
            );

            StockMovement movement = new StockMovement(
                    rs.getInt("id"),
                    value,
                    MovementTypeEnum.valueOf(rs.getString("type")),
                    rs.getTimestamp("creation_datetime").toInstant()
            );

            movements.add(movement);
        }

        return movements;
    }

    @Override
    public List<StockMovement> findStockMovementsByIngredientIdAndDateRange(int ingredientId, Instant from, Instant to) {
        String sql = "SELECT id, quantity, type, unit, creation_datetime " +
                     "FROM stock_movement " +
                     "WHERE id_ingredient = ? " +
                     "AND creation_datetime >= ? " +
                     "AND creation_datetime <= ? " +
                     "ORDER BY creation_datetime";

        List<StockMovement> movements = new ArrayList<>();
        Connection conn = dataSource.getDBConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ingredientId);
            ps.setTimestamp(2, Timestamp.from(from));
            ps.setTimestamp(3, Timestamp.from(to));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StockValue value = new StockValue(
                        rs.getDouble("quantity"),
                        Unit.valueOf(rs.getString("unit"))
                );

                StockMovement movement = new StockMovement(
                        rs.getInt("id"),
                        value,
                        MovementTypeEnum.valueOf(rs.getString("type")),
                        rs.getTimestamp("creation_datetime").toInstant()
                );

                movements.add(movement);
            }

            return movements;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.close(conn);
        }
    }

    @Override
    public List<StockMovement> createStockMovements(int ingredientId, List<StockMovement> movements) {
        String sql = "INSERT INTO stock_movement (id_ingredient, quantity, type, unit, creation_datetime) " +
                     "VALUES (?, ?, ?::movement_type, ?::unit_type, ?) " +
                     "RETURNING id, quantity, type, unit, creation_datetime";

        List<StockMovement> createdMovements = new ArrayList<>();
        Connection conn = dataSource.getDBConnection();

        try {
            conn.setAutoCommit(false);

            try {
                PreparedStatement ps = conn.prepareStatement(sql);

                for (StockMovement movement : movements) {
                    ps.setInt(1, ingredientId);
                    ps.setDouble(2, movement.getValue().getQuantity());
                    ps.setString(3, movement.getType().name());
                    ps.setString(4, movement.getValue().getUnit().name());
                    ps.setTimestamp(5, Timestamp.from(movement.getCreationDateTime()));

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        StockValue value = new StockValue(
                                rs.getDouble("quantity"),
                                Unit.valueOf(rs.getString("unit"))
                        );

                        StockMovement createdMovement = new StockMovement(
                                rs.getInt("id"),
                                value,
                                MovementTypeEnum.valueOf(rs.getString("type")),
                                rs.getTimestamp("creation_datetime").toInstant()
                        );

                        createdMovements.add(createdMovement);
                    }
                }

                conn.commit();
                return createdMovements;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            dataSource.close(conn);
        }
    }
}