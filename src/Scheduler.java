import java.util.*;

public class Scheduler {

    private final Map<Task, Integer> earliestFinishTimes;
    public Scheduler() {
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
            int par = task.U * task.deadline + task.V * task.computationCost;
            int maxPriority = 0;
            for(Task task1:task.pred){
                maxPriority = Math.max(maxPriority, task1.dependencyPriority);
            }
            if(task.isCritical)
                task.dependencyPriority = maxPriority + par;
            else
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
        Collections.reverse(dag.sortedTasks);
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
     * 遍历节点，获取关键路径
     */
    public void getCriticalPath(DAG dag) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

        for (Task task : dag.sortedTasks) {
            if (task.isRTTask)
                setCritical(task);
        }

        Set<Task> rt_tasks = new LinkedHashSet<>();
        Set<Task> common_tasks = new LinkedHashSet<>();
        Queue<Task> tmp = new LinkedList<>();
        tmp.offer(dag.entry);

//        dag.rt_task_priority_queue.add(dag.entry);
        rt_tasks.add(dag.entry);

        while(!tmp.isEmpty()){
            Task taskNow = tmp.poll();
            for(Task suc: taskNow.suc){
                tmp.offer(suc);
                if(suc.isCritical  && !suc.isScheduled){
//                    dag.rt_task_priority_queue.add(suc);
                    rt_tasks.add(suc);
                }else if (!suc.isScheduled){
//                    dag.task_priority_queue.add(suc);
                    common_tasks.add(suc);
                }

                suc.isScheduled = true;
            }
        }




        for(Task task: rt_tasks){
            System.out.print(task.getName() + " ");
        }
        System.out.println();
        for (Task task : common_tasks) {
            System.out.print(task.getName() + " ");
        }
    }

    private void setCritical(Task task){
        task.isCritical = true;

        if(task.pred.isEmpty())
            return;

        for(Task pred : task.pred){
            setCritical(pred);
        }
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
