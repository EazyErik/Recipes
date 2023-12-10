package recipes;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectionRepository extends CrudRepository<Direction,Integer> {


    Integer deleteByName(String name);
}
