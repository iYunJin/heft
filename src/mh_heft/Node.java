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
    int computationCost;
    long length;
    int deadline;
    long actualStartTime;
    long actualFinishTime;
    int rank;
    int priorityLevel;
    double priority;
    double U;
    double V;
    private boolean isCompleted;
    private Task task;
    boolean isCritical;
    boolean isScheduled;
    public boolean isRTTask;
    static final int HIGH_PRIORITY = 3;
    static final int MEDIUM_PRIORITY = 2;
    static final int LOW_PRIORITY = 1;

    static final int HIGH_PRIORITY_DEADLINE = 5;
    static final int MEDIUM_PRIORITY_DEADLINE = 10;
    static final int LOW_PRIORITY_DEADLINE = 15;

    private int remainingTime; // 剩余执行时间
    private int executedTime;  // 已执行时间

    // 后继节点
    List<Node> suc;
    // 前驱节点
    List<Node> pred;
    List<Node> usedPred;
    // 通信代价
    Map<Node,Integer> communicationCosts;

    public Node(String name, int computationCost,Task task,int priorityLevel){
        this.name = name;
        this.remainingTime = computationCost;
        this.computationCost = computationCost;
        this.priorityLevel = priorityLevel;
        this.length = computationCost * 1000L;
        this.U = 0.5;
        this.V = 0.5;
        this.pred = new ArrayList<>();
        this.usedPred = new ArrayList<>();
        this.suc = new ArrayList<>();
        this.communicationCosts = new HashMap<>();
        this.task = task;
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

    public void execute() {
//        System.out.println("task "+this.name+" is running\n");
//        int
//        if(task!=null) {
////            task.run();
//        }
        complete();
    }

    public String getName() {
        return name;
    }
    public int getComputationCost() {
        return computationCost;
    }
    public long getLength() {
        return length;
    }
    public int getDeadline() {
        return deadline;
    }
    public double getRank() {
        return rank;
    }
    public double getPriority() {
        return priority;
    }
    public boolean isRTTask() {
        return isRTTask;
    }
    public String getId() {
        return id;
    }

//    public int getExecutionTime() {
//        return computationCost;
//    }

    /**
     * 检查任务是否已经完成
     *
     * @return 如果任务已经完成，返回true，否则返回false
     */
    public boolean isCompleted() {
        return isCompleted;
    }
    /**
     * 标记任务为已完成
     */
    public void complete() {
        this.isCompleted = true;
    }

    /**
     * 检查所有依赖任务是否都已经完成
     *
     * @return 如果所有依赖任务都已经完成，返回true，否则返回false
     */
    public boolean allDependenciesCompleted() {
        for (Node dependency : this.usedPred)
            if (!dependency.isCompleted())
                return false;
        return true;
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

    public int getRemainingTime() {
        return remainingTime;
    }

    public void executeForTime(int time) {
        if (time > remainingTime) {
            executedTime += remainingTime;
            remainingTime = 0;
            complete();
        } else {
            executedTime += time;
            remainingTime -= time;
        }
    }

    public int getExecutedTime() {
        return executedTime;
    }
}
