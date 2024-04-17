import java.util.*;

public class TaskSort {

//    private final Map<Task, Integer> earliestFinishTimes;
    public TaskSort() {
//        this.earliestFinishTimes = new HashMap<>();
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
    public void DoTaskSort(DAG dag) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

        calculateRank(dag);
        //设置关键路径
        for (Task task : dag.sortedTasks) {
            if (task.isRTTask) {
                setCritical(task);
            }
        }
        //计算优先级
        calculateTaskPriority(dag);
        //分离关键路径和普通任务
        DAG[] dags = separateTasks(dag);
        //排序
        do_sort(dags[0]);
        do_sort(dags[1]);
        dag.sortedRTTasks = dags[0].sortedTasks;
        dag.sortedCommonTasks = dags[1].sortedTasks;
//        dags[0].printGraph();
//        dags[1].printGraph();
    }

    /**
     * 计算依赖优先级,并将其存储在Task类的dependencyPriority中
     * 依赖优先级 = 前驱节点的依赖优先级最大值
     */
    public void calculateTaskPriority(DAG dag) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

        for (Task task : dag.sortedTasks) {
            double par;
            if (task.isCritical) {
                // 实时任务的优先级由截止时间和计算成本得出
                par = task.U * task.deadline + task.V * task.rank;
            } else {
                // 普通任务的优先级由前置任务里是否有实时任务和计算成本决定
                boolean hasRTTaskPred = task.pred.stream().anyMatch(Task::isRTTask);
                par = task.U * (hasRTTaskPred ? 50 : 0) + task.V * task.rank;
            }

//            double maxPriority = 0;
//
//            for (Task task1 : task.pred) {
//                maxPriority = Math.max(maxPriority, task1.priority);
//            }
            task.priority =  par;
        }
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
            pred.deadline = pred.deadline == 0 ? task.deadline :Math.min(task.deadline,pred.deadline);
            setCritical(pred);
        }
    }

    /**
     * 分离关键路径和普通任务
     * @param dag DAG图
     * @return 分离后的DAG数组
     */
    private DAG[] separateTasks(DAG dag) {
        int commonNum=0,rtNum=0;
        DAG[] dags = new DAG[2];
        dags[0] = new DAG("rt_dag");
        dags[1] = new DAG("common_dag");

        Task entry = new Task("virtual_entry", 0);
        dags[1].addTask(entry);

        for (Task task : dag.sortedTasks) {
            if (task.isCritical) {
                dags[0].addTask(task);
                rtNum++;
                task.suc.removeIf(suc -> !suc.isCritical);
            } else {
                commonNum++;
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
        dag.rtTaskNum = rtNum;
        dag.commonTaskNum = commonNum;
        return dags;
    }


    /**
     * 根据任务的优先级进行排序，优先级高的任务在队列前面
     */
    private void do_sort(DAG dag) {
        if(dag.sortedTasks == null)
            dag.topologicalSort();

        PriorityQueue<Task> taskQueue = new PriorityQueue<>((t1, t2) -> Double.compare(t2.getPriority(), t1.getPriority()));

        taskQueue.addAll(dag.sortedTasks);

        List<Task> scheduledTasks = new ArrayList<>();

        while (!taskQueue.isEmpty()) {
            Iterator<Task> iterator = taskQueue.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (new HashSet<>(scheduledTasks).containsAll(task.pred)) {
                    scheduledTasks.add(task);
                    iterator.remove();
                    break;
                }
            }
        }

        dag.sortedTasks = scheduledTasks;
    }

//    public void calculateEarliestStartTime(DAG dag,Processor processor) {
//        if (dag.sortedTasks == null)
//            dag.topologicalSort();
////        for(Task task : sortedTasks){
////            int maxStartTime = 0;
////            for(Task pred : task.pred){
////                maxStartTime = Math.max(maxStartTime, earliestFinishTimes.get(pred)+pred.communicationCosts.get(task));
////            }
////            task.earliestStartTime = maxStartTime;
////            earliestFinishTimes.put(task, maxStartTime + task.computationCost);
////        }
//    }
}
