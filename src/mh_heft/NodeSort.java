package mh_heft;

import java.util.*;

public class NodeSort {

    private double minCost = 0;
    private double maxCost = 0;
    private double minDeadline = 0;
    private double maxDeadline = 0;


    public NodeSort() {
    }
    /**
     * 遍历节点，获取关键路径
     */
    public void DoTaskSort(DAG dag) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

        //计算任务的截止完成时间
        for (Node task : dag.sortedTasks) {
            task.deadline = calculateDeadline(task);
            if(task.priorityLevel == Node.HIGH_PRIORITY)
                task.deadline *= 1.1;
            else if(task.priorityLevel == Node.MEDIUM_PRIORITY)
                task.deadline *= 1.2;
            else
                task.deadline *= 1.3;
        }

        //设置关键路径
        for (Node task : dag.sortedTasks) {
            if (task.priorityLevel == Node.HIGH_PRIORITY){
                setCritical(task);
            }
            minCost = task.computationCost < minCost ? task.computationCost : minCost;
            maxCost = task.computationCost > maxCost ? task.computationCost : maxCost;
            maxDeadline = task.deadline > maxDeadline ? task.deadline : maxDeadline;
            minDeadline = task.deadline < minDeadline ? task.deadline : minDeadline;
        }
        //计算优先级
        calculateTaskPriority(dag);
        //分离关键路径和普通任务
        DAG[] dags = separateTasks(dag);
//        排序
        do_sort(dags[0],dag);
        do_sort(dags[1],dag);

//        dag.sortedRTTasks = dags[0].sortedTasks;
//        dag.sortedCommonTasks = dags[1].sortedTasks;

    }

    private int calculateDeadline(Node task){
        if(task.pred.isEmpty()){
            return 0;
        }
        int maxDead = 0;
        for(Node pred : task.pred){
            int deadline  = calculateDeadline(pred) + pred.communicationCosts.get(task);
            maxDead = Math.max(maxDead,deadline );
        }
        return maxDead + task.computationCost;
    }

    /**
     * 计算依赖优先级,并将其存储在Task类的dependencyPriority中
     * 依赖优先级 = 前驱节点的依赖优先级最大值
     */
    public void calculateTaskPriority(DAG dag) {
        if (dag.sortedTasks == null)
            dag.topologicalSort();

        for (Node task : dag.sortedTasks) {
            double normalizedCost = (task.getComputationCost() - minCost) / (maxCost - minCost);
            double normalizedDeadline = (task.deadline - minDeadline) / (maxDeadline - minDeadline);

            if(task.priority == Node.HIGH_PRIORITY){
                task.U = 0.7;
                task.V = 0.3;
            }else if(task.priority == Node.MEDIUM_PRIORITY){
                task.U = 0.6;
                task.V = 0.4;
            }else {
                task.U = 0.5;
                task.V = 0.5;
            }
            task.priority = task.priorityLevel+ task.U * normalizedDeadline + task.V * normalizedCost;
        }
    }


    /**
     * 递归设置关键路径
     * @param task 当前任务
     */
    private void setCritical(Node task){
        task.isCritical = true;
        task.priorityLevel = Node.HIGH_PRIORITY;

        if(task.pred.isEmpty())
            return;

        for(Node pred : task.pred){
//            pred.deadline = pred.deadline == 0 ? task.deadline :Math.min(task.deadline,pred.deadline);
            setCritical(pred);
        }
    }

    /**
     * 分离关键路径和普通任务
     * @param dag DAG图
     * @return 分离后的DAG数组
     */
    private DAG[] separateTasks(DAG dag) {
        int commonNum = 0, rtNum = 0;

        DAG[] dags = new DAG[2];

        dags[0] = new DAG("rt_dag");
        dags[1] = new DAG("common_dag");

//        Node entry = new Node("virtual_entry", 0,null,Node.MEDIUM_PRIORITY);
//        dags[1].addTask(entry);
//        commonNum++;
        //
        for (Node task : dag.sortedTasks) {
            if (task.isCritical) {
                rtNum++;
                dags[0].addTask(task);
                task.suc.removeIf(suc -> !suc.isCritical);
            } else {
                commonNum++;
                dags[1].addTask(task);
                task.pred.removeIf(pred -> pred.isCritical);
            }
        }

//        dags[0].entry = dag.entry;
//        dags[0].topologicalSort();
        dags[1].topologicalSort();
        dags[0].rtTaskNum = rtNum;
        dags[1].commonTaskNum = commonNum;

        if(rtNum == 0 ){
            dags[0] = null;
        }

        if (commonNum ==0) {
            dags[1] = null;
        }
//        dags[1].topologicalSort();


//        Node entry = new Node("virtual_entry", 0, null, Node.MEDIUM_PRIORITY);
//        dags[1].addTask(entry);
//        commonNum++;
//        dags[0].entry = dag.entry;
//        dags[1].topologicalSort();
//        dags[1].entry = entry;
        //添加虚拟入口节点
//        if (dags[1].entry.suc.isEmpty()) {
//            for (Node task : dags[1].sortedTasks) {
//                if (task.pred.isEmpty() && task != entry) {
//                    dags[1].addDependency(entry, task, 0);
//                }
//            }
//        }

        return dags;
    }


    /**
     * 根据任务的优先级进行排序，优先级高的任务在队列前面
     */
    private void do_sort(DAG dag,DAG originalDag) {

        if(dag == null)
            return;

        if(dag.sortedTasks == null)
            dag.topologicalSort();

        PriorityQueue<Node> taskQueue = new PriorityQueue<>((t1, t2) -> Double.compare(t2.getPriority(), t1.getPriority()));

        taskQueue.addAll(dag.sortedTasks);

        List<Node> scheduledTasks = new ArrayList<>();

        while (!taskQueue.isEmpty()) {

            Iterator<Node> iterator = taskQueue.iterator();
            while (iterator.hasNext()) {
                Node task = iterator.next();
                if (new HashSet<>(scheduledTasks).containsAll(task.pred)) {
                    scheduledTasks.add(task);
                    iterator.remove();
                    break;
                }
            }
        }

        dag.sortedTasks = scheduledTasks;

        if(dag.rtTaskNum != 0) {
            originalDag.sortedRTTasks = dag.sortedTasks;
            originalDag.rtTaskNum += dag.rtTaskNum;
        }

        if(dag.commonTaskNum != 0) {
            originalDag.sortedCommonTasks = dag.sortedTasks;
            originalDag.commonTaskNum += dag.commonTaskNum;
        }
    }

}
