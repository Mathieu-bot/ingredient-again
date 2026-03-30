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

    private Dish mapDish(ResultSet rs) throws SQLException {
        return new Dish(
                rs.getInt("id"),
                rs.getString("name"),
                DishTypeEnum.valueOf(rs.getString("dish_type")),
                rs.getObject("price") != null ? rs.getDouble("price") : null
        );
    }
}