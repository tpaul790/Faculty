public class Mall {
    private String name;
    private String manager;
    private int year;
    private double profit;

    public Mall(String name, String manager, int year, double profit) {
        this.name = name;
        this.manager = manager;
        this.year = year;
        this.profit = profit;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }

    public int getYear() {
        return year;
    }

    public double getProfit() {
        return profit;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Manager: " + manager + ", Year: " + year + ", Profit: " + profit;
    }

    public void setProfit(double v) {
        profit = v;
    }
}
