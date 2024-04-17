import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MultiDagScheduler{
    int RtTaskNum;
    int CommonTaskNum;
    private List<DagScheduler> schedulers;
    private List<DAG> dags;
    public Queue<Task> ready_queue;

    public MultiDagScheduler() {
        schedulers = new ArrayList<>();
        dags = new ArrayList<>();
        ready_queue = new LinkedList<>();
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

    public void schedule() {
        while(RtTaskNum > 0) {
            Task inTask = null;
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
            Task inTask = null;
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
}