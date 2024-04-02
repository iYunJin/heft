/**
 * Processor class to store the name, speed, and cost of a processor.
 */
public class Processor {
    private String name;
    private int speed;
    private int cost;

    public Processor(String name, int speed, int cost) {
        this.name = name;
        this.speed = speed;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCost() {
        return cost;
    }
}
