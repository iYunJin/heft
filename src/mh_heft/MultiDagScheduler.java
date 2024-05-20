package mh_heft;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiDagScheduler{
    int RtTaskNum;
    int CommonTaskNum;
    private List<DagScheduler> schedulers;
    private List<DAG> dags;
    private List<Processor> processors;
    public Queue<Node> ready_queue;

    public MultiDagScheduler() {
        schedulers = new ArrayList<>();
        dags = new ArrayList<>();
        ready_queue = new ConcurrentLinkedQueue<>();
        processors = new ArrayList<>();
    }

    public void addDag(DAG dag) {
        dags.add(dag);
    }

    /**
     * 根据已添加的dag进行任务的排序和分离入列。
     */
    public void task_job() {
        for (DAG dag : dags) {
            DagScheduler scheduler = new DagScheduler();
            schedulers.add(scheduler);
            scheduler.schedule(dag);
            RtTaskNum += dag.rtTaskNum;
            CommonTaskNum += dag.commonTaskNum;
        }
    }

    /**
     * 所有dag任务入列
     */
    public void task_in_queue() {
        while(RtTaskNum > 0) {
            Node inTask = null;
            DagScheduler tmpScheduler = null;
            for (DagScheduler scheduler : schedulers) {
                if (scheduler.rt_task_priority_queue.isEmpty())
                    continue;
                if (inTask == null) {
                    inTask = scheduler.rt_task_priority_queue.peek();
                    tmpScheduler = scheduler;
                }

                if (scheduler.rt_task_priority_queue.peek().getPriority() > inTask.getPriority()) {
                    inTask = scheduler.rt_task_priority_queue.peek();
                    tmpScheduler = scheduler;
                }

            }
            if (tmpScheduler != null) {
                ready_queue.offer(tmpScheduler.rt_task_priority_queue.poll());
            }
            RtTaskNum--;
        }

        while(CommonTaskNum > 0) {
            Node inTask = null;
            DagScheduler tmpScheduler = null;
            for (DagScheduler scheduler : schedulers) {
                if (scheduler.common_task_priority_queue.isEmpty())
                    continue;
                if (inTask == null) {
                    inTask = scheduler.common_task_priority_queue.peek();
                    tmpScheduler = scheduler;
                }

                if (scheduler.common_task_priority_queue.peek().getPriority() > inTask.getPriority()) {
                    inTask = scheduler.common_task_priority_queue.peek();
                    tmpScheduler = scheduler;
                }
            }
            if (tmpScheduler != null) {
                ready_queue.offer(tmpScheduler.common_task_priority_queue.poll());
            }
            CommonTaskNum--;
        }

    }

    /**
     * 添加处理器
     * @param processor 处理器
     */
    public void addProcessor(Processor processor) {
        processors.add(processor);
    }

    /**
     * 调度任务到处理器
     */
    public void scheduleTasks() {
        // 尝试调度等待队列中的任务
        while (!ready_queue.isEmpty()) {
            Iterator<Node> iterator = ready_queue.iterator();
            while (iterator.hasNext()) {
                Node task = iterator.next();
                if (!task.allDependenciesCompleted()) {
                    continue;
                }
                Processor minEftProcessor = findMinEFTProcessor(task);
                if (minEftProcessor != null) {
//                    task.actualStartTime = System.currentTimeMillis();
                    // 将任务调度到处理器
                    minEftProcessor.schedule(task);
                    iterator.remove();
                }
            }
        }
    }

//    public void scheduleTasks2() {
//        while (!ready_queue.isEmpty()) {
//            Node task = ready_queue.peek();
//            if (task.allDependenciesCompleted()) {
//                Processor minEftProcessor = findMinEFTProcessor(task);
//                if (minEftProcessor != null) {
//                    // 将任务调度到处理器
//                    minEftProcessor.schedule(task);
//                }
//                ready_queue.poll();
//            }
//        }
//    }


    /**
     * 查找具有最小EFT的处理器
     *
     * @param task 要调度的任务
     * @return 具有最小EFT的处理器
     */
    private Processor findMinEFTProcessor(Node task) {
        Processor minEftProcessor = null;
        long minEft = Integer.MAX_VALUE;

        for (Processor processor : processors) {
            long eft = calculateEFTWithCommunicationCost(processor,task);
            if (eft < minEft) {
                minEft = eft;
                minEftProcessor = processor;
            }
        }
        return minEftProcessor;
    }

    private long calculateEFTWithCommunicationCost(Processor processor, Node newTask) {
        long eft = processor.calculateEFT(newTask);
        for (Node pred : newTask.getPred()) {
            if (!pred.getProcessorName().equals(processor.getName())) {
                eft += pred.getCommunicationCost(newTask);
            }
        }
        return eft;
    }

}