package mh_heft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务类
 */
public class Node {
    private String name;
    private String processorName;
    String id;
    public int computationCost;
//    long length;
    public int deadline;
//    int deadlineLevel;
    long actualStartTime;
    long actualFinishTime;
    public double rank;
    public long slack;
    public int priorityLevel;
    double priority;
    double U;
    double V;
    private boolean isCompleted;
    private Task task;
    boolean isCritical;
    boolean isScheduled;
//    public boolean isRTTask;
    public static final int HIGH_PRIORITY = 3;
    public static final int MEDIUM_PRIORITY = 2;
    public static final int LOW_PRIORITY = 1;

    // 后继节点
    List<Node> suc;
    // 前驱节点
    public List<Node> pred;
    List<Node> usedPred;

    boolean isVisited = false;
    // 通信代价
    public Map<Node,Integer> communicationCosts;

    public Node(String name, int computationCost,Task task,int priorityLevel){
        this.name = name;
//        this.remainingTime = computationCost;
        this.computationCost = computationCost;
        this.priorityLevel = priorityLevel;
//        this.length = computationCost * 1000L;
        this.pred = new ArrayList<>();
        this.usedPred = new ArrayList<>();
        this.suc = new ArrayList<>();
        this.communicationCosts = new HashMap<>();
        this.task = task;

//        if(priorityLevel== HIGH_PRIORITY){
//            this.deadline = HIGH_PRIORITY_DEADLINE;
//        }else if(priorityLevel == MEDIUM_PRIORITY){
//            this.deadline = MEDIUM_PRIORITY_DEADLINE;
//        }else{
//            this.deadline = LOW_PRIORITY_DEADLINE;
//        }

    }

    /**
     * 添加通信代价
     * @param to 目标节点
     * @param cost 通信代价
     */
    public void addCommunicationCost(Node to, int cost) {
        communicationCosts.put(to, cost);
    }

    /**
     * 添加后继节点
     * @param succession 后继节点
     */
    public void addSuc(Node succession){
        suc.add(succession);
    }

    /**
     * 添加前驱节点
     * @param pred 前驱节点
     */
    public void addPred(Node pred){
        this.pred.add(pred);
        this.usedPred.add(pred);
    }

//    public void execute() {
//        this.actualStartTime = System.currentTimeMillis();
//        try {
//            Thread.sleep(computationCost); // Simulate execution time
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        this.actualFinishTime = System.currentTimeMillis();
//        this.isCompleted = true;
//    }

    public boolean allDependenciesCompleted() {
        for (Node dependency : this.usedPred)
            if (!dependency.isCompleted())
                return false;
        return true;
    }
    public String getName() {
        return name;
    }
    public int getComputationCost() {
        return computationCost;
    }
//    public long getLength() {
//        return length;
//    }
    public int getDeadline() {
        return deadline;
    }
//    public double getRank() {
//        return rank;
//    }
    public double getPriority() {
        return priority;
    }
//    public boolean isRTTask() {
//        return isRTTask;
//    }
    public String getId() {
        return id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessor(String processor) {
        this.processorName = processor;
    }

    public int getCommunicationCost(Node to){
        return communicationCosts.get(to);
    }

    public List<Node> getSuc() {
        return suc;
    }

    public List<Node> getPred() {
        return pred;
    }

    public Task getTask() {
        return task;
    }

//    public int getRemainingTime() {
//        return remainingTime;
//    }

//    public void executeForTime(double time) {
//        if (time > remainingTime) {
//            executedTime += remainingTime;
//            remainingTime = 0;
//        } else {
//            executedTime += time;
//            remainingTime -= time;
//        }
//    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

//    public int getExecutedTime() {
//        return executedTime;
//    }

    public int calculateSlack(int currentTime) {
        return deadline - (currentTime + getComputationCost());
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
