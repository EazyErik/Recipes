package recipes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component

@Data
public class Service {


    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    DirectionRepository directionRepository;
    @Autowired
    IngredientRepository ingredientRepository;

    private RecipeDTO recipeConverterToDTO(Recipe recipe){
     return new RecipeDTO(recipe.getName(),recipe.getDescription(),convertIngredientToString(recipe.getIngredients()),convertDirectionToString(recipe.getDirections()));

    }
    private List<String> convertIngredientToString(List<Ingredient> ingredients){
        List<String> strings = new ArrayList<>();
        for(Ingredient ingredient : ingredients){
            strings.add(ingredient.getName());
        }
        return strings;
    }

    private List<String> convertDirectionToString(List<Direction> directions){
        List<String> strings = new ArrayList<>();
        for(Direction direction : directions){
            strings.add(direction.getName());
        }
        return strings;
    }



    private Recipe recipeDTOConverterToRecipe(RecipeDTO recipeDTO){
        return new Recipe(recipeDTO.getName(),recipeDTO.getDescription());

        //return new Recipe(recipeDTO.getName(),recipeDTO.getDescription(),convertStringToIngredient(recipeDTO.getIngredients()),convertStringToDirection(recipeDTO.getDirections()));
    }

    private List<Ingredient> convertStringToIngredient(List<String> strings,Recipe recipe){
        List<Ingredient> ingredients = new ArrayList<>();
        for(String string : strings){
            Ingredient ingredient = new Ingredient();
            ingredient.setName(string);
            ingredient.setRecipe(recipe);
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    private List<Direction> convertStringToDirection(List<String> strings,Recipe recipe){
        List<Direction> directions = new ArrayList<>();
        for(String string : strings){
            Direction direction = new Direction();
            direction.setName(string);
            direction.setRecipe(recipe);
            directions.add(direction);
        }
        return directions;
    }

    public RecipeDTO getRecipeById(int id){
        //List<Recipe> optionalRecipes = recipes.stream().filter(recipe -> id == recipe.getId()).collect(Collectors.toList());
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if(optionalRecipe.isPresent()){
            return recipeConverterToDTO(optionalRecipe.get());
        }
        return null;


    }

    public IdDTO addNewRecipe(RecipeDTO recipe){

        Recipe savedRecipe = recipeRepository.save(recipeDTOConverterToRecipe(recipe));

        List<Ingredient> ingredients = convertStringToIngredient(recipe.getIngredients(),savedRecipe);
        List<Direction> directions = convertStringToDirection(recipe.getDirections(),savedRecipe);

        for(Direction direction : directions){
            directionRepository.save(direction);
        }

        for(Ingredient ingredient : ingredients){
            ingredientRepository.save(ingredient);
        }

        return new IdDTO(savedRecipe.getId());

    }

    public boolean deleteById(int id) {
        RecipeDTO recipeById = getRecipeById(id);
        if(recipeById == null){
            return false;
        }
        recipeRepository.deleteById(id);
        return true;
    }
}
