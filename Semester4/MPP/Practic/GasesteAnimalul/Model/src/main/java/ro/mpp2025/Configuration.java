package ro.mpp2025;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Configurations")
public class Configuration implements Identity<Integer>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "row")
    private int row;
    @Column(name = "col")
    private int column;
    @Column(name = "animal")
    private String animal;
    @Column(name = "path")
    private String path;

    public Configuration() {}

    public Configuration(int row, int column, String animal, String path) {
        this.row = row;
        this.column = column;
        this.animal = animal;
        this.path = path;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        id = integer;
    }
}
