package recipes;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Collections;
import java.util.Comparator;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

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
            String nameT= recipe.getName();
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

    public IdDTO addNewRecipe(RecipeDTO recipe) {
        Recipe savedRecipe = recipeDTOConverterToRecipe(recipe);

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

    public boolean deleteById(int id) {
        RecipeDTO recipeById = getRecipeById(id);
        if (recipeById == null) {
            return false;
        }
        recipeRepository.deleteById(id);
        return true;
    }

    public RecipeDTO updateRecipe(int id, RecipeDTO recipe) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isEmpty()) {
            return null;
        }
        Recipe targetedRecipe = optionalRecipe.get();

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
        return recipeConverterToDTO(targetedRecipe);
    }

    @Transactional
    public RecipeDTO updateRecipe1(int id, RecipeDTO newRecipeDTO) {
        System.out.println("id : " + id);
        System.out.println(newRecipeDTO.getIngredients());
        RecipeDTO oldRecipeDTO = getRecipeById(id);
        if (oldRecipeDTO == null) {
            return null;
        }
        newRecipeDTO.setDate(LocalDateTime.now());
        Recipe newRecipe = recipeDTOConverterToRecipe(newRecipeDTO);
        newRecipe.setId(id);

        Iterable<Ingredient> tempIngredients = ingredientRepository.findAll();
        List<Ingredient> oldIngredient = new ArrayList<>();
        tempIngredients.forEach((value) -> {
            System.out.println("test");
            if (value.getRecipe().getId() == id) {
                System.out.println();
                oldIngredient.add(value);
            }
        });

        for (Ingredient ingredient : oldIngredient) {
            boolean check = false;
            for (String newIngredient : newRecipeDTO.getIngredients()) {
                System.out.println("1");
                if (newIngredient.equals(ingredient.getName())) {
                    check = true;
                    System.out.println("2");
                }
                System.out.println("3");
                // System.out.println("new ingredient: " + newIngredient + ", check: " + check + ", ingredient: " + ingredient);
            }
            System.out.println("4");
            if (!check) {

                ingredientRepository.delete(ingredient);


            }
            System.out.println("5");

        }


        /*for (String ingredient : oldRecipeDTO.getIngredients()) {
            boolean check = false;
            for (String newIngredient : newRecipeDTO.getIngredients()) {
                if (newIngredient.equals(ingredient)) {
                    check = true;
                }
                System.out.println("new ingredient: " + newIngredient + ", check: " + check + ", ingredient: " + ingredient);
            }
            if (!check) {
//                System.out.println("deleted: " + ingredient);
                deleteIngredientByNameAndRecipeId(ingredient, id);


            }
//            if (!newRecipeDTO.getIngredients().contains(ingredient)) {
//                System.out.println("i am here");
//
//
//               Integer ingredientId  = ingredientRepository.getByNameAndRecipeId(ingredient, id);
//                System.out.println(ingredientId);
//                ingredientRepository.deleteById(ingredientId);
//            }
        }
*/
        for (String ingredient : newRecipeDTO.getIngredients()) {
            if (!oldRecipeDTO.getIngredients().contains(ingredient)) {
                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(ingredient);
                newIngredient.setRecipe(newRecipe);
                ingredientRepository.save(newIngredient);
            }
        }

        for (String direction : oldRecipeDTO.getDirections()) {
            if (!newRecipeDTO.getIngredients().contains(direction)) {
//todo:org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [delete From recipes.Ingredient where name = ?1 and recipe_id = ?2]
                directionRepository.deleteByName(direction);
            }
        }


        for (String direction : newRecipeDTO.getDirections()) {
            if (!oldRecipeDTO.getDirections().contains(direction)) {
                Direction newDirection = new Direction();
                newDirection.setName(direction);
                newDirection.setRecipe(newRecipe);
                directionRepository.save(newDirection);
            }
        }

        recipeRepository.save(newRecipe);

        return newRecipeDTO;
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
