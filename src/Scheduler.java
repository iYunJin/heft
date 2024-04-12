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
    public void calculateTaskPriority(DAG dag) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

        for (Task task : dag.sortedTasks) {
            double par = task.U * task.deadline + task.V * task.computationCost;

            if(task.isCritical){
                task.priority = par;
                continue;
            }

            double maxPriority = 0;
            for(Task task1:task.pred){
                maxPriority = Math.max(maxPriority, task1.priority);
            }

            task.priority = maxPriority + par;
        }
    }

    /**
     * 计算任务的rank
     */
    private void calculateRank(DAG dag) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

        Collections.reverse(dag.sortedTasks);
        //递归调用计算
        for (Task task : dag.sortedTasks) {
            task.rank = upwardRank(task);
        }
        Collections.reverse(dag.sortedTasks);
    }
    private int upwardRank(Task task) {
        if (task.suc.isEmpty()) {
            return task.computationCost;
        }
        int maxRank = 0;
        for (Task suc : task.suc) {
            int rank = upwardRank(suc) + task.communicationCosts.get(suc);
            maxRank = Math.max(maxRank, rank);
        }
        return maxRank + task.computationCost;
    }


    /**
     * 遍历节点，获取关键路径
     */
    public void TaskSort(DAG dag) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

//        calculateRank(dag);
        //设置关键路径
        for (Task task : dag.sortedTasks) {
            if (task.isRTTask) {
                setCritical(task);
                setDeadline(task);
            }
        }
        calculateTaskPriority(dag);

        //分离关键路径和普通任务
        DAG[] dags = separateTasks(dag);

        dag.rt_task_priority_queue.offer(dags[0].entry);



//        dags[0].printGraph();



//        Set<Task> rt_tasks = new LinkedHashSet<>();
//        Set<Task> common_tasks = new LinkedHashSet<>();
//        Queue<Task> tmp = new LinkedList<>();
//        tmp.offer(dag.entry);
//
//        rt_tasks.add(dag.entry);
//
//        while(!tmp.isEmpty()){
//            Task taskNow = tmp.poll();
//            for(Task suc: taskNow.suc){
//                tmp.offer(suc);
//                if(suc.isCritical  && !suc.isScheduled){
////                    dag.rt_task_priority_queue.add(suc);
//                    rt_tasks.add(suc);
//                }else if (!suc.isScheduled){
////                    dag.task_priority_queue.add(suc);
//                    common_tasks.add(suc);
//                }
//                suc.isScheduled = true;
//            }
//        }
//
//        DAG rt_dag = new DAG("rt_dag");
//        DAG common_dag = new DAG("common_dag");
//
//        for(Task task: rt_tasks){
//            rt_dag.addTask(task);
//        }
//
//
//
//        for(Task task: rt_tasks){
//            System.out.print(task.getName() + " ");
//        }
//        System.out.println();
//        for (Task task : common_tasks) {
//            System.out.print(task.getName() + " ");
//        }
    }

    /**
     * 递归设置关键路径
     * @param task 当前任务
     */
    private void setCritical(Task task){
        task.isCritical = true;

        if(task.pred.isEmpty())
            return;

        for(Task pred : task.pred){
            setCritical(pred);
        }
    }

    /**
     * 设置deadline
     * @param task 当前任务
     */
    private void setDeadline(Task task){
        if(task.pred.isEmpty()){
            return;
        }
        for(Task pred : task.pred) {
            pred.deadline = Math.min(task.deadline,pred.deadline);
            setCritical(pred);
        }
    }

    /**
     * 分离关键路径和普通任务
     * @param dag DAG图
     * @return 分离后的DAG数组
     */
    private DAG[] separateTasks(DAG dag) {
        DAG[] dags = new DAG[2];
        dags[0] = new DAG("rt_dag");
        dags[1] = new DAG("common_dag");

        Task entry = new Task("virtual_entry", 0);
        dags[1].addTask(entry);

        for (Task task : dag.sortedTasks) {
            if (task.isCritical) {
                dags[0].addTask(task);
                task.suc.removeIf(suc -> !suc.isCritical);
            } else {
                dags[1].addTask(task);
                task.pred.removeIf(pred -> pred.isCritical);
            }
        }

        dags[0].entry= dag.entry;
        dags[1].topologicalSort();
        dags[1].entry = entry;
        for (Task task : dags[1].sortedTasks)
            if (task.pred.isEmpty() && task != entry) {
                dags[1].addDependency(entry, task, 0);
            }
        return dags;
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
