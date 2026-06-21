package chpt.cookbook.controller;

import chpt.cookbook.entity.Recipe;
import chpt.cookbook.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/")
        public String home(@RequestParam(required = false) String category,
                           @RequestParam(required = false) String search,
                           Model model)
    {
        List<Recipe> recipes;

        if (search != null && !search.isEmpty()) {
            recipes = recipeRepository.findByDishNameContainingIgnoreCase(search);
        }
        else if (category != null && !category.isEmpty()) {
            recipes = recipeRepository.findByCategory(category);
        }
        else {
            recipes = recipeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("search", search);

        return "main";
    }
}

