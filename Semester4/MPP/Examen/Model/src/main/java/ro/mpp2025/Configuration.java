package ro.mpp2025;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Configurations")
public class Configuration implements Identity<Integer>,Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(mappedBy = "configuration",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Animal> animals;

    public Configuration() {
        animals = new ArrayList<>();
    }

    public Configuration(List<Animal> animals) {
        this.animals = animals;

    }

    public void addToAnimals(Animal animal) {
        animals.add(animal);
        animal.setConfiguration(this);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        id = integer;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }
}
