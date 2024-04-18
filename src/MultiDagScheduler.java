import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
        ready_queue = new LinkedList<>();
        processors = new ArrayList<>();
    }

    public void addDag(DAG dag) {
        dags.add(dag);
    }

    /**
     * dag节点入列
     */
    public void inQueue() {
        for (DAG dag : dags) {
            DagScheduler scheduler = new DagScheduler();
            schedulers.add(scheduler);
            scheduler.schedule(dag);
            RtTaskNum += dag.rtTaskNum;
            CommonTaskNum += dag.commonTaskNum;
        }
    }

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
        while (!ready_queue.isEmpty()) {
            // 从队列中取出任务
            Node task = ready_queue.poll();

            // 找到一个可用的处理器
            Processor minEftProcessor = findMinEFTProcessor(task);
            if (minEftProcessor == null) {
                // 如果没有可用的处理器，就等待一会儿
                try {
                    Thread.sleep(1000); // 等待1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            // 将任务调度到处理器
            minEftProcessor.schedule(task);
        }
    }

//    /**
//     * 查找一个可用的处理器
//     * @return 可用的处理器
//     */
//    private Processor findAvailableProcessor() {
//        for (Processor processor : processors) {
//            if (processor.isAvailable()) {
//                return processor;
//            }
//        }
//        return null;
//    }

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