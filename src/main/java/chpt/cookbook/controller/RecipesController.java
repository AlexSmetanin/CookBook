package chpt.cookbook.controller;

import chpt.cookbook.entity.Recipe;
import chpt.cookbook.entity.RecipeDto;
import chpt.cookbook.repository.RecipeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
//@RequestMapping("/recipes")
public class RecipesController {

    public static  ArrayList<Recipe> recipesList = new ArrayList<>();

    @Autowired
    private RecipeRepository recipesRepository;

    @GetMapping({"/recipes"})
    public String showRecipesList(
            @RequestParam(required = false) String category,
            Model model) {
        List<Recipe> recipes = recipesRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("recipes", recipes);
        return "recipes/recipes";
    }

    @GetMapping("/recipes/create")
    public String showCreateRecipeForm(Model model) {
        RecipeDto recipeDto = new RecipeDto();
        model.addAttribute("recipeDto", recipeDto);
        return "recipes/createRecipe";
    }

    @PostMapping("/recipes/create")
    public String createRecipe(
            @Valid @ModelAttribute RecipeDto recipeDto,
            BindingResult result) {
        if (recipeDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "Файл зображення обов'язковий"));
        }

        if (result.hasErrors()) {
            return "recipes/createRecipe";
        }

        // Зберігаємо файл картинки
        MultipartFile image = recipeDto.getImageFile();
        Date createAt = new Date();
        String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir= "public/images";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            System.out.println("Exception:" + ex.getMessage());
        }

        Recipe recipe = new Recipe();
        recipe.setDishName(recipeDto.getDishName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCategory(recipeDto.getCategory());
        recipe.setIngredients(recipeDto.getIngredients());
        recipe.setInstruction(recipeDto.getInstruction());
        recipe.setCalories(recipeDto.getCalories());
        recipe.setImageFileName(storageFileName);

        recipesRepository.save(recipe);

        return "redirect:/recipes";
    }

    @GetMapping("/recipes/edit")
    public String showEditRecipeForm(
            Model model,
            @RequestParam int id) {
        try {
            Recipe recipe = recipesRepository.findById((long) id).get();
            model.addAttribute("recipe", recipe);

            RecipeDto recipeDto = new RecipeDto();
            recipeDto.setDishName(recipe.getDishName());
            recipeDto.setDescription(recipe.getDescription());
            recipeDto.setCategory(recipe.getCategory());
            recipeDto.setIngredients(recipe.getIngredients());
            recipeDto.setInstruction(recipe.getInstruction());
            recipeDto.setCalories(recipe.getCalories());

            model.addAttribute("recipeDto", recipeDto);
        }
        catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
            return "recipes";
        }

        return "recipes/editRecipe";
    }

    @PostMapping("/recipes/edit")
    public String updateRecipe(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute RecipeDto recipeDto,
            BindingResult result
    ) {

        try {
            Recipe recipe = recipesRepository.findById((long) id).get();
            model.addAttribute("recipe", recipe);

            if (result.hasErrors()) {
                return "recipes/editRecipe";
            }

            if (!recipeDto.getImageFile().isEmpty()) {
                // видалити старе зображення
                String uploadDir= "public/images";
                Path oldImagePath = Paths.get(uploadDir + recipe.getImageFileName());

                try {
                    Files.delete(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Exception:" + ex.getMessage());
                }

                // зберегти новий файл зображення
                MultipartFile image = recipeDto.getImageFile();
                Date updateAt = new Date();
                String storageFileName = updateAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir+storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                recipe.setImageFileName(storageFileName);
            }
            recipe.setDishName(recipeDto.getDishName());
            recipe.setDescription(recipeDto.getDescription());
            recipe.setCategory(recipeDto.getCategory());
            recipe.setIngredients(recipeDto.getIngredients());
            recipe.setInstruction(recipeDto.getInstruction());
            recipe.setCalories(recipeDto.getCalories());
            recipesRepository.save(recipe);
        }
        catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }

        return "redirect:/recipes";
    }

    @GetMapping("/recipes/delete")
    public String deleteRecipe(
            @RequestParam int id
    ) {
        try {
            Recipe recipe = recipesRepository.findById((long) id).get();
            // вилучення зображення товару
            Path imagePath = Paths.get("public/images/" + recipe.getImageFileName());
            try {
                Files.delete(imagePath);
            } catch (Exception ex) {
                System.out.println("Exception:" + ex.getMessage());
            }

            // вилучення товару
            recipesRepository.delete(recipe);

        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
        return "redirect:/recipes";
    }

    @GetMapping("/recipes/details")
    public String showRecipeDetailForm(
            Model model,
            @RequestParam int id) {
        try {
            Recipe recipe = recipesRepository.findById((long) id).get();
            model.addAttribute("recipe", recipe);
        }
        catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
            return "main";
        }

        return "recipes/recipeDetails";
    }
}
