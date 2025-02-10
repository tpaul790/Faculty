//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Car car = new Car(2020,400.5);
        AudiCar audiCar = new AudiCar(2022,1500,"Germany");
        PorcheCar porcheCar = new PorcheCar(2024,2020,"Macan");
        Car anotherPorcheCar = new PorcheCar(2021,2222,"Taycan");

        Car[] cars = {car,audiCar,porcheCar,anotherPorcheCar};
        
        for(var c : cars){
            System.out.println(c.toString());
        }
    }
}