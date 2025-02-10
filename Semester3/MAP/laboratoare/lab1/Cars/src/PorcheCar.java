public class PorcheCar extends Car {
    private String model;

    PorcheCar(int year,double price,String model) {
        super(year,price);
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public String toString() {
        return (super.toString() + " | Model: " + model);
    }
}
