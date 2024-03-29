import java.util.HashMap;
import java.util.Map;

public class Task {
    String name;
    int computationCost;
    double rank;
    Map<Task,Integer> communicationCosts;

    public Task(String name, int computationCost) {
        this.name = name;
        this.computationCost = computationCost;
        this.communicationCosts = new HashMap<>();
    }

    public void addCommunicationCost(Task to, int cost) {
        communicationCosts.put(to, cost);
    }
}
