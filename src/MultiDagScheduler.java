import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiDagScheduler{
    int TaskNum;
    int RtTaskNum;
    int CommonTaskNum;
    private List<DagScheduler> schedulers;
    private List<DAG> dags;
    private List<Processor> processors;
    public Queue<Node> rt_ready_queue;
    public Queue<Node> cm_ready_queue;
    public Queue<Node> wait_queue;

    public MultiDagScheduler() {
        schedulers = new ArrayList<>();
        dags = new ArrayList<>();
        rt_ready_queue = new ConcurrentLinkedQueue<>();
        cm_ready_queue = new ConcurrentLinkedQueue<>();
        processors = new ArrayList<>();
        wait_queue = new ConcurrentLinkedQueue<>();
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
                rt_ready_queue.offer(tmpScheduler.rt_task_priority_queue.poll());
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
                cm_ready_queue.offer(tmpScheduler.common_task_priority_queue.poll());
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
        while (!rt_ready_queue.isEmpty() || !wait_queue.isEmpty() || !cm_ready_queue.isEmpty()) {

            // 尝试调度等待队列中的任务
            for(Node task : wait_queue) {
//                Node task = wait_queue.peek();
                if (!task.allDependenciesCompleted()) {
                    continue;
                }

                Processor minEftProcessor = findMinEFTProcessor(task);
                if (minEftProcessor != null) {
                    minEftProcessor.schedule(task);
                    wait_queue.remove(task);
                }
            }

            // 从就绪队列中取出任务进行调度
            if (!rt_ready_queue.isEmpty()) {
                Node task = rt_ready_queue.poll();
                if (task.allDependenciesCompleted()) {
                    Processor minEftProcessor = findMinEFTProcessor(task);
                    if (minEftProcessor != null) {
                        // 将任务调度到处理器
                        minEftProcessor.schedule(task);
                    }
                } else {
                    // 如果任务的依赖任务还未完成，将任务加入等待队列
                    wait_queue.offer(task);
                }
                continue;
            }
//            if (!cm_ready_queue.isEmpty()){
//                Node task = cm_ready_queue.poll();
//                if (task.allDependenciesCompleted()) {
//                    Processor minEftProcessor = findMinEFTProcessor(task);
//                    if (minEftProcessor != null) {
//                        // 将任务调度到处理器
//                        minEftProcessor.schedule(task);
//                    }
//                } else {
//                    // 如果任务的依赖任务还未完成，将任务加入等待队列
//                    wait_queue.offer(task);
//                }
//            }
        }
    }


    /**
     * 查找具有最小EFT的处理器
     *
     * @param task 要调度的任务
     * @return 具有最小EFT的处理器
     */
    private Processor findMinEFTProcessor(Node task) {
        Processor minEftProcessor = null;
        int minEft = Integer.MAX_VALUE;
        for (Processor processor : processors) {
            int eft = processor.calculateEFT(task);
            if (eft < minEft) {
                minEft = eft;
                minEftProcessor = processor;
            }
        }
        return minEftProcessor;
    }
}