package mh_heft;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * mh_heft.Processor class to store the name, speed, and cost of a processor.
 */
public class Processor {
    private String name;
    public Queue<Node> taskQueue;
    private Node currentTask;
    public int speed;
    public Processor() {
        this.name = "mh_heft.Processor";
        taskQueue = new ConcurrentLinkedQueue<>();
        currentTask = null;
    }
    public Processor(String name) {
        this.name = name;
        taskQueue = new ConcurrentLinkedQueue<>();
        currentTask = null;
    }

    /**
     * 计算预期完成时间（EFT）
     * @param newTask 新的任务
     * @return 预期完成时间
     */
    public int calculateEFT(Node newTask) {
        int eft = 0;
        for (Node task : taskQueue) {
            eft += task.getExecutionTime();
        }
        if (currentTask != null) {
            eft += currentTask.getExecutionTime();
        }
        eft += newTask.getExecutionTime() /speed;
        return eft;
    }

    /**
     * 将任务调度到处理器
     * @param task 要调度的任务
     */
    public void schedule(Node task) {
        taskQueue.offer(task);
        task.setProcessor(this.name);
//        eft += task.computationCost;
//        if (!taskQueue.isEmpty()) {
//            eft += task.communicationCost;
//        }
    }

    /**
     * 执行处理器的下一个任务
     */
    public void execute() {
        if(taskQueue.isEmpty()) {
            return;
        }
        // 如果当前没有正在执行的任务，那么从任务队列中取出一个任务开始执行
//        if(currentTask == null || currentTask.isCompleted()) {
        currentTask = taskQueue.poll();
//        if (currentTask != null) {
        currentTask.isScheduled = true;
        currentTask.actualStartTime = System.currentTimeMillis();
        currentTask.execute();
        currentTask.actualFinishTime = System.currentTimeMillis();
//        }
//        }
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }
}
