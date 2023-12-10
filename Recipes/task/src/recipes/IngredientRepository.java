package recipes;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient,Integer> {

    Integer deleteByName(String name);

     @Query("select id From Ingredient where name like ?1 and recipe_id = ?2")
     Integer getIdByName(String name, int id);

    @Override
    void deleteById(Integer integer);
}
