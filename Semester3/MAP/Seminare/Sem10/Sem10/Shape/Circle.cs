namespace Sem10;

public class Circle : Shape {
    private double radius { get; set; }

    public Circle(double radius) {
        this.radius = radius;
    }
    
    public double computeArea() {
        return Math.PI * radius * radius;
    }

    public string toString() {
        return $"radius: {radius} | compute area: {computeArea()}";
    }

}