package recipes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {

    private String name;
    private String description;
    private List<String> ingredients;
    private List<String> directions;
    private String category;
    private LocalDateTime date;

}
