package recipes;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient,Integer> {

    Integer deleteByName(String name);

    @Query("delete From Ingredient where name = ?1 and recipe_id = ?2")
    Integer deleteByNameAndRecipeId(String name, int id);

}
