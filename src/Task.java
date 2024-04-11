import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务类
 */
public class Task {
    private String name;
    int computationCost;
    int deadline;
    int dependencyPriority;
    // 最早开始时间
    int earliestStartTime;

    // makeSpan 任务完成时间
    int makeSpan;
    double rank;
    double priority;
    int U;
    int V;

    boolean isCritical;
    boolean isScheduled;
    boolean isRTTask;
    // 后继节点
    List<Task> suc;
    // 前驱节点
    List<Task> pred;
    // 通信代价
    Map<Task,Integer> communicationCosts;

    public Task(String name, int computationCost) {
        this.name = name;
        this.computationCost = computationCost;
        this.U = 1;
        this.V = 1;
        this.pred = new ArrayList<>();
        this.suc = new ArrayList<>();
        this.communicationCosts = new HashMap<>();
    }

    /**
     * 添加通信代价
     * @param to 目标节点
     * @param cost 通信代价
     */
    public void addCommunicationCost(Task to, int cost) {
        communicationCosts.put(to, cost);
    }

    /**
     * 添加后继节点
     * @param succession 后继节点
     */
    public void addSuc(Task succession){
        suc.add(succession);
    }

    /**
     * 添加前驱节点
     * @param pred 前驱节点
     */
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

    public double getPriority() {
        return priority;
    }

}
