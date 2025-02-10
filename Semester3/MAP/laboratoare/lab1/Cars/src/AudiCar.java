public class AudiCar extends Car {
    private String market;

    public AudiCar(int year, double price, String market){
        super(year, price);
        this.market = market;
    }

    public String getMarket(){
        return market;
    }

    public String toString(){
        return (super.toString() +  " | Market: " + market);
    }
}
