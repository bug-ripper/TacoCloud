package com.denisbondd111.tacocloud.repository;

import com.denisbondd111.tacocloud.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
