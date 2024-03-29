import java.util.*;

public class HEFTScheduler {
    DAG dag;
    Map<Task, Integer> earliestFinishTimes;

    public HEFTScheduler(DAG dag) {
        this.dag = dag;
        this.earliestFinishTimes = new HashMap<>();
    }

    public void schedule() {

        upwardRank();

        List<Task> tasks = dag.getTasks();

        Comparator<Task> rankComparator = Comparator.comparingDouble(task->-task.rank);

        tasks.sort(rankComparator);


        System.out.println("Schedule:");
        for (Task task : tasks) {
            //System.out.println("Task: " + task.name + ", Earliest Finish Time: " + earliestFinishTimes.get(task));
            System.out.println("Task: " + task.name + ", Rank: " + task.rank);
        }
    }

    public void upwardRank() {
        List<Task> tasks = dag.topologicalSort();
        Collections.reverse(tasks);

        for (Task task : tasks) {
            task.rank = upwardRank(task);
        }
    }

    private double upwardRank(Task task) {
        if (task.rank > 0) {
            return task.rank;
        }

        if (task.pred.isEmpty()) {
            return task.computationCost;
        }
        double maxRank = 0;
        for (Task predecessor : task.pred) {
            double rank = upwardRank(predecessor) + task.communicationCosts.get(predecessor);
            maxRank = Math.max(maxRank, rank);
        }

        return maxRank + task.computationCost;
    }

//    private int getPriority(Task task) {
//        int priority = task.computationCost;
//        for (Task predecessor : dag.getDependencies(task)) {
//            priority += task.communicationCosts.get(predecessor);
//        }
//        return priority;
//    }

}
