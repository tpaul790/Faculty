package ro.mpp2025;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Attempts")
public class Attempt implements Identity<Integer>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "row")
    private int row;
    @Column(name = "col")
    private int column;
    @ManyToOne
    @JoinColumn(name = "idGame", nullable = false)
    private Game game;

    public Attempt() {}

    public Attempt(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Game setPlayer() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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
}
