import java.util.*;

public class HEFTScheduler {

    private final Map<Task, Integer> earliestFinishTimes;
    public HEFTScheduler() {
        this.earliestFinishTimes = new HashMap<>();
    }

    /**
     * 计算依赖优先级,并将其存储在Task类的dependencyPriority中
     * 依赖优先级 = 前驱节点的依赖优先级最大值
     */
    public void calculateDependencyPriority(DAG dag) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

        for (Task task : dag.sortedTasks) {
            int maxPriority = 0;
            for (Task pred : task.pred) {
                maxPriority = Math.max(maxPriority, pred.dependencyPriority);
            }
            task.dependencyPriority = maxPriority;
        }
    }

    /**
     * 计算任务的rank
     */
    public void calculateRank(DAG dag) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

        Collections.reverse(dag.sortedTasks);
        //递归调用计算
        for (Task task : dag.sortedTasks) {
            task.rank = upwardRank(task);
        }
    }
    private double upwardRank(Task task) {
        if (task.suc.isEmpty()) {
            return task.computationCost;
        }
        double maxRank = 0;
        for (Task suc : task.suc) {
            double rank = upwardRank(suc) + task.communicationCosts.get(suc);
            maxRank = Math.max(maxRank, rank);
        }
        return maxRank + task.computationCost;
    }


    /**
     * 计算任务的最早开始时间
     */
    public void calculateEarliestStartTime(DAG dag,Processor processor) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

//        for(Task task : sortedTasks){
//            int maxStartTime = 0;
//            for(Task pred : task.pred){
//                maxStartTime = Math.max(maxStartTime, earliestFinishTimes.get(pred)+pred.communicationCosts.get(task));
//            }
//            task.earliestStartTime = maxStartTime;
//            earliestFinishTimes.put(task, maxStartTime + task.computationCost);
//        }
    }


    /**
     * 计算任务的优先级
     * 优先级 = a * deadline + b * rank + c * dependencyPriority
     */
//    public void calculatePriority(double a,double b,double c) {
//        if (sortedTasks == null)
//            topologicalSort();
//
//        for (Task task : sortedTasks) {
//            task.priority = a * task.deadline + b * task.rank + c * task.dependencyPriority;
//        }
//    }
}
