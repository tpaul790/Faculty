public class Car {
    private int year;
    private double price;

    public Car(){};

    public Car(int year, double price){
        this.year = year;
        this.price = price;
    }

    public int getYear(){
        return year;
    }

    public double getPrice(){
        return price;
    }

    public String toString(){
        return("Year: " + year + " | Price: " + price);
    }
}
