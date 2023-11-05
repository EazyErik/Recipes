package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    Service service;

    @PostMapping("/recipe/new")
    public ResponseEntity<IdDTO> addRecipe(@RequestBody RecipeDTO recipeDTO){
        System.out.println(recipeDTO.getName() + "," + recipeDTO.getDescription());

        if(recipeDTO.getName() == null || recipeDTO.getDescription() == null ||
                recipeDTO.getDirections() == null
            || recipeDTO.getIngredients() == null ||
                recipeDTO.getName().isEmpty() || recipeDTO.getDescription().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<IdDTO>( service.addNewRecipe(recipeDTO),HttpStatus.OK);

//todo:POST /api/recipe/new should respond with status code 400, responded: 200
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable int id){
        RecipeDTO recipe = service.getRecipeById(id);
        if(recipe != null){
            return new ResponseEntity<>(recipe,HttpStatus.OK);
        }else{
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<RecipeDTO> deleteRecipeById(@PathVariable int id){
        boolean isDeleted = service.deleteById(id);
        if(isDeleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }
}
