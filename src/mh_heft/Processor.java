package mh_heft;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * mh_heft.Processor class to store the name, speed, and cost of a processor.
 */
public class Processor implements Runnable {
    private String name;
    public Queue<Node> taskQueue;
    private Node currentTask;
    public long mips = 1000L;
    public int speed;
    public boolean running;
    private long timeSlice = 1; // 模拟的时间片
    public Processor() {
        this.name = "mh_heft.Processor";
        taskQueue = new ConcurrentLinkedQueue<>();
        currentTask = null;
    }
    public Processor(String name,int speed) {
        this.name = name;
        taskQueue = new ConcurrentLinkedQueue<>();
        currentTask = null;
        this.speed = speed;
        this.running = true;
    }

    /**
     * 计算预期完成时间（EFT）
     * @param newTask 新的任务
     * @return 预期完成时间
     */
    public int calculateEFT(Node newTask) {
        int eft = 0;
        for (Node task : taskQueue) {
            eft += task.getRemainingTime();
        }
        if (currentTask != null) {
            eft += currentTask.getRemainingTime();
        }
        eft += newTask.getComputationCost() /speed;
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
        if(currentTask != null ){
            return;
        }

        if (taskQueue.isEmpty())
            return;

        currentTask = taskQueue.poll();
        if (currentTask != null) {
            currentTask.isScheduled = true;
            long executionTime = currentTask.getComputationCost() / speed;
            currentTask.actualStartTime = System.currentTimeMillis();
            while(executionTime > 0) {
                try {
                    Thread.sleep(timeSlice);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                currentTask.executeForTime((int) timeSlice);
                executionTime -= timeSlice;
            }
//            currentTask.execute();
            currentTask.complete();
            currentTask.actualFinishTime = System.currentTimeMillis();
            currentTask = null;
        }
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        while (running) {
            execute();
            try {
                Thread.sleep(10); // Prevent busy-waiting
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public void stop() {
        this.running = false;
    }
}
