package com.hei.prog3.ingredientagain.repository.impl;

import com.hei.prog3.ingredientagain.config.DataSource;
import com.hei.prog3.ingredientagain.entity.CategoryEnum;
import com.hei.prog3.ingredientagain.entity.Ingredient;
import com.hei.prog3.ingredientagain.repository.IngredientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcIngredientRepository implements IngredientRepository {

    private final DataSource dataSource;

    public JdbcIngredientRepository(DataSource dataSource) {
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

            throw new RuntimeException("Ingredient not found (id=" + id + ")");
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
}