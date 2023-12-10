package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    Service service;

    @PostMapping("/recipe/new")
    public ResponseEntity<IdDTO> addRecipe(@RequestBody RecipeDTO recipeDTO) {


        if (
                recipeDTO.getName() == null ||
                        recipeDTO.getName().isBlank() ||
                        recipeDTO.getDescription() == null ||
                        recipeDTO.getDirections() == null ||
                        recipeDTO.getDescription().isBlank() ||
                        recipeDTO.getIngredients() == null ||
                        recipeDTO.getIngredients().isEmpty() ||
                        recipeDTO.getDirections().isEmpty() ||
                        recipeDTO.getCategory() == null ||
                        recipeDTO.getCategory().isBlank()
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<IdDTO>(service.addNewRecipe(recipeDTO), HttpStatus.OK);


    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Void> updateRecipe(@PathVariable int id, @RequestBody RecipeDTO recipeDTO) {
        if (
                recipeDTO.getName() == null ||
                        recipeDTO.getName().isBlank() ||
                        recipeDTO.getDescription() == null ||
                        recipeDTO.getDirections() == null ||
                        recipeDTO.getDescription().isBlank() ||
                        recipeDTO.getIngredients() == null ||
                        recipeDTO.getIngredients().isEmpty() ||
                        recipeDTO.getDirections().isEmpty() ||
                        recipeDTO.getCategory() == null ||
                        recipeDTO.getCategory().isBlank()
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        RecipeDTO updatedRecipe = service.updateRecipe(id, recipeDTO);
        if (updatedRecipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable int id) {
        RecipeDTO recipe = service.getRecipeById(id);
        if (recipe != null) {
            return new ResponseEntity<>(recipe, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/recipe/search")
    public ResponseEntity<List<RecipeDTO>> getRecipes(@RequestParam(required = false) String name, @RequestParam(required = false) String category) {
        boolean invalidName = name == null || name.isBlank();
        boolean invalidCategory = category == null || category.isBlank();
        if (invalidName && invalidCategory) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!invalidName && !invalidCategory) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(service.filter(name, category), HttpStatus.OK);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<RecipeDTO> deleteRecipeById(@PathVariable int id) {
        boolean isDeleted = service.deleteById(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }
}
