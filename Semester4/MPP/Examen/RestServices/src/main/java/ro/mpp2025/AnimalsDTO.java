// src/main/java/ro/mpp2025/dto/AnimalsDTO.java
package ro.mpp2025;

import java.util.List;

public class AnimalsDTO {
    private List<String> animals;

    public AnimalsDTO() {}

    public AnimalsDTO(List<String> animals) {
        this.animals = animals;
    }

    public List<String> getAnimals() {
        return animals;
    }

    public void setAnimals(List<String> animals) {
        this.animals = animals;
    }
}
