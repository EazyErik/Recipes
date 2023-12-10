package recipes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direction> directions;
    private String category;
    private LocalDateTime date;


    public Recipe(String name, String description, String category, LocalDateTime date) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        if (this.ingredients == null) {
            this.ingredients = ingredients;
        } else {
            this.ingredients.retainAll(ingredients);
            this.ingredients.addAll(ingredients);
        }
    }

    public void setDirections(List<Direction> directions) {
        if (this.ingredients == null) {
            this.directions = directions;
        } else {
            this.directions.retainAll(directions);
            this.directions.addAll(directions);
        }
    }
}
