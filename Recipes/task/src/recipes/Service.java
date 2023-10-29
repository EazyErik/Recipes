package recipes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component

@Data
public class Service {

    private List<Recipe> recipes = new ArrayList<>();

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



    private Recipe recipeDTOConverterToRecipe(RecipeDTO recipeDTO,int id){
        return new Recipe(id,recipeDTO.getName(),recipeDTO.getDescription(),convertStringToIngredient(recipeDTO.getIngredients()),convertStringToDirection(recipeDTO.getDirections()));
    }

    private List<Ingredient> convertStringToIngredient(List<String> strings){
        List<Ingredient> ingredients = new ArrayList<>();
        for(String string : strings){
            Ingredient ingredient = new Ingredient();
            ingredient.setName(string);
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    private List<Direction> convertStringToDirection(List<String> strings){
        List<Direction> directions = new ArrayList<>();
        for(String string : strings){
            Direction direction = new Direction();
            direction.setName(string);
            directions.add(direction);
        }
        return directions;
    }

    public RecipeDTO getRecipeById(int id){
        List<Recipe> optionalRecipes = recipes.stream().filter(recipe -> id == recipe.getId()).collect(Collectors.toList());
        return optionalRecipes.size() > 0 ? recipeConverterToDTO(optionalRecipes.get(0)) : null;

    }

    public IdDTO addNewRecipe(RecipeDTO recipe){
        int id = recipes.size() + 1;
        recipes.add(recipeDTOConverterToRecipe(recipe,id));
        return new IdDTO(id);
    }
    //todo:Repository aufsetzen mit dazugehoerigen Methoden, alle Converter muessen getestet werden

}
