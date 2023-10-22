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

        return new ResponseEntity<IdDTO>( service.addNewRecipe(recipeDTO),HttpStatus.OK);
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
}
