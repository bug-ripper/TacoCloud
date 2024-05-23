package com.denisbondd111.tacocloud.controller;

import com.denisbondd111.tacocloud.domain.Ingredient;
import com.denisbondd111.tacocloud.domain.Ingredient.Type;
import com.denisbondd111.tacocloud.domain.Taco;
import com.denisbondd111.tacocloud.domain.TacoOrder;
import com.denisbondd111.tacocloud.repository.IngredientRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/design")
@Controller
@SessionAttributes("tacoOrder")
public class DesignTacoController {
    private final IngredientRepository ingredientRepository;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model){
        List<Ingredient> ingredients = (List<Ingredient>) ingredientRepository.findAll();

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }

    @ModelAttribute("tacoOrder")
    public TacoOrder order(Model model){
        return new TacoOrder();
    }

    @ModelAttribute("taco")
    public Taco taco(Model model){
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(){
        return "design";
    }

    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder){
        if(errors.hasErrors()){
            return "design";
        }
        tacoOrder.addTaco(taco);
        log.info("Submited Taco: {}", taco);
        return "redirect:/orders/current";
    }

    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
