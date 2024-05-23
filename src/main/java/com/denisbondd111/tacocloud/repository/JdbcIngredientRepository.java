package com.denisbondd111.tacocloud.repository;

import com.denisbondd111.tacocloud.domain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcIngredientRepository implements IngredientRepository{
    private final String SQL_FIND_ALL = "select id, name, type from Ingredient";
    private final String SQL_FIND_BY_ID = "select id, name, type from Ingredient where id=?";
    private final String SQL_SAVE = "insert into Ingredient (id, name, type) values (?, ?, ?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, this::mapRowToIngredient);
    }

    @Override
    public Optional<Ingredient> findById(String id) {
        List<Ingredient> result = jdbcTemplate.query(SQL_FIND_BY_ID, this::mapRowToIngredient, id);
        return result.size() == 0 ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update(
                SQL_SAVE,
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString());
        return ingredient;
    }

    private Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {
        return new Ingredient(
                row.getString("id"),
                row.getString("name"),
                Ingredient.Type.valueOf(row.getString("type")));
    }
}
