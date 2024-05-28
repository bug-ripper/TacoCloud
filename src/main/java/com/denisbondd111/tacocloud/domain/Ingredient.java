package com.denisbondd111.tacocloud.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@Entity
public class Ingredient {
    @Id
    private String id;

    private String name;

    private Type type;

    public Ingredient() {

    }

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

}
