package recipes;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


import java.util.Collections;
import java.util.Comparator;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static recipes.UpdateDeleteStatus.*;

@Component

@Data
public class Service {


    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    DirectionRepository directionRepository;
    @Autowired
    IngredientRepository ingredientRepository;

    private RecipeDTO recipeConverterToDTO(Recipe recipe) {
        return new RecipeDTO(
                recipe.getName(),
                recipe.getDescription(),
                convertIngredientToString(recipe.getIngredients()),
                convertDirectionToString(recipe.getDirections()),
                recipe.getCategory(),
                recipe.getDate());

    }

    private List<String> convertIngredientToString(List<Ingredient> ingredients) {
        List<String> strings = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            strings.add(ingredient.getName());
        }
        return strings;
    }

    private List<String> convertDirectionToString(List<Direction> directions) {
        List<String> strings = new ArrayList<>();
        for (Direction direction : directions) {
            strings.add(direction.getName());
        }
        return strings;
    }


    private Recipe recipeDTOConverterToRecipe(RecipeDTO recipeDTO) {
        return new Recipe(recipeDTO.getName(), recipeDTO.getDescription(), recipeDTO.getCategory(), LocalDateTime.now());
    }

    public RecipeDTO getRecipeById(int id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            return recipeConverterToDTO(optionalRecipe.get());
        }
        return null;
    }

    public List<Recipe> getAllRecipes() {
        Iterable<Recipe> recipes = recipeRepository.findAll();
        List<Recipe> list = new ArrayList<>();
        recipes.forEach(list::add);
        return list;
    }

    public List<RecipeDTO> filter(String name, String category) {
        List<Recipe> recipes = getAllRecipes();
        List<Recipe> filteredRecipes = new ArrayList<>();
        List<RecipeDTO> finalResults = new ArrayList<>();
        if (name != null && !name.isBlank()) {
            filteredRecipes.addAll(filterByName(name, recipes));
        } else if (category != null && !category.isBlank()) {
            filteredRecipes.addAll(filterByCategory(category, recipes));
        }
        for (Recipe recipe : filteredRecipes) {
            finalResults.add(recipeConverterToDTO(recipe));
        }
        Collections.sort(finalResults, Comparator.comparing(RecipeDTO::getDate).reversed());

        return finalResults;
    }

    public List<Recipe> filterByName(String name, List<Recipe> recipes) {
        List<Recipe> results = new ArrayList<>();
        for (Recipe recipe : recipes) {
            String nameT = recipe.getName();
            if (nameT.toLowerCase().contains(name.toLowerCase())) {
                results.add(recipe);
            }
        }
        return results;
    }

    public List<Recipe> filterByCategory(String category, List<Recipe> recipes) {
        List<Recipe> results = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getCategory().equalsIgnoreCase(category)) {
                results.add(recipe);
            }
        }
        return results;
    }

    public IdDTO addNewRecipe(RecipeDTO recipe, String authorEmail) {
        Recipe savedRecipe = recipeDTOConverterToRecipe(recipe);
        savedRecipe.setAuthorEmail(authorEmail);

        List<Direction> directions = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();

        for (String directionName : recipe.getDirections()) {
            Direction direction = new Direction(directionName, savedRecipe);
            directions.add(direction);
        }

        for (String ingredientName : recipe.getIngredients()) {
            Ingredient ingredient = new Ingredient(ingredientName, savedRecipe);
            ingredients.add(ingredient);
        }
        savedRecipe.setDirections(directions);
        savedRecipe.setIngredients(ingredients);
        savedRecipe.setDate(LocalDateTime.now());

        savedRecipe = recipeRepository.save(savedRecipe);

        return new IdDTO(savedRecipe.getId());

    }

    public UpdateDeleteStatus deleteById(int id, String authorEmail) {
        //RecipeDTO recipeById = getRecipeById(id);
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);

        if (optionalRecipe.isEmpty()) {
            return RECIPE_NOT_FOUND;
        }
        Recipe targetedRecipe = optionalRecipe.get();
        if (authorEmail.equalsIgnoreCase(targetedRecipe.getAuthorEmail())){
            recipeRepository.deleteById(id);
            return SUCCEEDED;
        }

       return UNAUTHORIZED;
    }

    public UpdateDeleteStatus updateRecipe(int id, RecipeDTO recipe, String authorEmail) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isEmpty()) {
            return RECIPE_NOT_FOUND;
        }

        Recipe targetedRecipe = optionalRecipe.get();

        if (!authorEmail.equalsIgnoreCase(targetedRecipe.getAuthorEmail())) {
            return UNAUTHORIZED;
        }

        targetedRecipe.setCategory(recipe.getCategory());
        targetedRecipe.setDescription(recipe.getDescription());
        targetedRecipe.setName(recipe.getName());
        targetedRecipe.setDate(LocalDateTime.now());

       /* targetedRecipe.getDirections().clear();
        targetedRecipe.getIngredients().clear();*/

        List<Direction> directions = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();


        for (String directionName : recipe.getDirections()) {
            Direction direction = new Direction(directionName, targetedRecipe);
            directions.add(direction);
        }

        for (String ingredientName : recipe.getIngredients()) {
            Ingredient ingredient = new Ingredient(ingredientName, targetedRecipe);
            ingredients.add(ingredient);
        }
        targetedRecipe.setDirections(directions);
        targetedRecipe.setIngredients(ingredients);

        targetedRecipe = recipeRepository.save(targetedRecipe);
        return SUCCEEDED;
    }


    @Transactional
    public void deleteIngredientByNameAndRecipeId(String name, int id) {
        Integer ingredientId = ingredientRepository.getIdByName(name, id);
        System.out.println(name);
        System.out.println(id);
        System.out.println(ingredientId);
        ingredientRepository.deleteById(ingredientId);

    }

}
