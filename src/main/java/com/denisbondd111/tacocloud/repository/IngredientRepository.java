package com.denisbondd111.tacocloud.repository;

import com.denisbondd111.tacocloud.domain.Ingredient;

import java.util.Optional;

public interface IngredientRepository {
    Iterable<Ingredient> findAll();
    Optional<Ingredient> findById(String id);
    Ingredient save(Ingredient ingredient);
}
