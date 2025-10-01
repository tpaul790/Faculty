package ro.mpp2025;

public class GameDTO {
    private int nrOfAttempts;
    private Attempt[] attempts;
    private int row,column;

    public GameDTO(int nrOfAttempts, Attempt[] attempts, int row, int column) {
        this.nrOfAttempts = nrOfAttempts;
        this.attempts = attempts;
        this.row = row;
        this.column = column;
    }

    public int getNrOfAttempts() {
        return nrOfAttempts;
    }

    public void setNrOfAttempts(int nrOfAttempts) {
        this.nrOfAttempts = nrOfAttempts;
    }

    public Attempt[] getAttempts() {
        return attempts;
    }

    public void setAttempts(Attempt[] attempts) {
        this.attempts = attempts;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
