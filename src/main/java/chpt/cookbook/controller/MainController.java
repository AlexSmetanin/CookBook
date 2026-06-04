package chpt.cookbook.controller;

import chpt.cookbook.entity.Recipe;
import chpt.cookbook.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Recipe> recipes = recipeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("recipes", recipes);
        return "main";
    }
}

