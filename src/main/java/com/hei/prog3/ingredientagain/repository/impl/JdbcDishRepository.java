package com.hei.prog3.ingredientagain.repository.impl;

import com.hei.prog3.ingredientagain.config.DataSourceConfig;
import com.hei.prog3.ingredientagain.entity.*;
import com.hei.prog3.ingredientagain.repository.DishRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcDishRepository implements DishRepository {

    private final DataSourceConfig dataSource;

    public JdbcDishRepository(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Dish> findAll() {
        String sql = "SELECT id, name, dish_type, price FROM dish";

        List<Dish> dishes = new ArrayList<>();
        Connection conn = dataSource.getDBConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Dish dish = mapDish(rs);
                dishes.add(dish);
            }

            return dishes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.close(conn);
        }
    }

    @Override
    public List<Dish> findAllWithIngredients() {
        String sql = "SELECT id, name, dish_type, price FROM dish";

        List<Dish> dishes = new ArrayList<>();
        Connection conn = dataSource.getDBConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Dish dish = mapDish(rs);

                List<DishIngredient> dishIngredients = findDishIngredientsByDishId(conn, dish.getId());
                dish.setDishIngredients(dishIngredients);

                dishes.add(dish);
            }

            return dishes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.close(conn);
        }
    }

    private Dish mapDish(ResultSet rs) throws SQLException {
        return new Dish(
                rs.getInt("id"),
                rs.getString("name"),
                DishTypeEnum.valueOf(rs.getString("dish_type")),
                rs.getObject("price") != null ? rs.getDouble("price") : null
        );
    }

    private List<DishIngredient> findDishIngredientsByDishId(Connection conn, int dishId) throws SQLException {
        String sql = """
                SELECT di.id_dish,
                       di.id_ingredient,
                       di.quantity_required,
                       di.unit,
                       i.id AS ingredient_id,
                       i.name AS ingredient_name,
                       i.price AS ingredient_price,
                       i.category
                FROM dish_ingredient di
                JOIN ingredient i ON i.id = di.id_ingredient
                WHERE di.id_dish = ?
                """;

        List<DishIngredient> dishIngredients = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, dishId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Ingredient ingredient = new Ingredient(
                    rs.getInt("ingredient_id"),
                    rs.getString("ingredient_name"),
                    rs.getDouble("ingredient_price"),
                    CategoryEnum.valueOf(rs.getString("category").toUpperCase()),
                    null,
                    null
            );

            DishIngredient dishIngredient = new DishIngredient(
                    null,
                    ingredient,
                    rs.getDouble("quantity_required"),
                    Unit.valueOf(rs.getString("unit"))
            );

            dishIngredients.add(dishIngredient);
        }

        return dishIngredients;
    }
}