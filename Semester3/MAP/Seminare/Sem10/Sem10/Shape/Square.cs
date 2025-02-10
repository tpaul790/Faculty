namespace Sem10;

public class Square : Shape {
    private int side { get; set; }

    public Square(int side) {
        this.side = side;
    }
    
    public double computeArea() {
        return side * side;
    }

    public string toString()
    {
        return $"Side: {side} | Area: {computeArea()}";
    }
}
