package chpt.cookbook.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class RecipeDto {
    @NotEmpty(message = "Назва обов'язкове поле")
    private String dishName;

    @Size(max = 2000, message = "Опис не може перевищувати 2000 символів")
    private String description;

    @Size(max = 200, message = "Категорія не може перевищувати 200 символів")
    private String category;

    @Size(max = 2000, message = "Інгредієнти не можуть перевищувати 2000 символів")
    private String ingredients;

    @Size(max = 2000, message = "Інструкції не можуть перевищувати 2000 символів")
    private String instruction;

    @Min(0)
    private Integer calories;

    private MultipartFile imageFile;

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
