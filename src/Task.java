import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Task类改为接口
 */


public class Task {
    String name;
    int computationCost;
    int deadline;
    int dependencyPriority;
    double rank;
    int priority;
    List<Task> suc;
    List<Task> pred;

    Map<Task,Integer> communicationCosts;

    public Task(String name, int computationCost) {
        this.name = name;
        this.computationCost = computationCost;
        this.pred = new ArrayList<>();
        this.suc = new ArrayList<>();
        this.communicationCosts = new HashMap<>();
    }

    public void addCommunicationCost(Task to, int cost) {
        communicationCosts.put(to, cost);
    }

    public void addSuc(Task succession){
        suc.add(succession);
    }

    public void addPred(Task pred){
        this.pred.add(pred);
    }

    public String getName() {
        return name;
    }
    public int getComputationCost() {
        return computationCost;
    }

    public int getDeadline() {
        return deadline;
    }

    public int getDependencyPriority() {
        return dependencyPriority;
    }

    public double getRank() {
        return rank;
    }

    public int getPriority() {
        return priority;
    }
}
