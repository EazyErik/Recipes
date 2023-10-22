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
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Service {

    private List<Recipe> recipes = new ArrayList<>();

    private RecipeDTO recipeConverterToDTO(Recipe recipe){
        return new RecipeDTO(recipe.getName(),recipe.getDescription(),recipe.getIngredients(),recipe.getDirections());

    }

    private Recipe recipeDTOConverterToRecipe(RecipeDTO recipeDTO,int id){
        return new Recipe(id,recipeDTO.getName(),recipeDTO.getDescription(),recipeDTO.getIngredients(),recipeDTO.getDirections());
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

}
