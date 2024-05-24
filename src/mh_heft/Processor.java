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
    public double speed;
    public boolean running;
    private final long timeSlice = 5; // 模拟的时间片

    private double earliestFinishTime = 0;
    public Processor() {
        this.name = "mh_heft.Processor";
        taskQueue = new ConcurrentLinkedQueue<>();
        currentTask = null;
    }
    public Processor(String name,double speed) {
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
    public double calculateEFT(Node newTask) {
//        int eft = 0;
//        for (Node task : taskQueue) {
//            eft += task.getRemainingTime();
//        }
//        if (currentTask != null) {
//            eft += currentTask.getRemainingTime();
//        }
//        eft += newTask.getComputationCost() /speed;
//        return eft;
        return earliestFinishTime + (newTask.getComputationCost() / speed);
    }

    /**
     * 将任务调度到处理器
     * @param task 要调度的任务
     */
    public void schedule(Node task) {
        taskQueue.offer(task);
        task.setProcessor(this.name);
        earliestFinishTime += (task.getComputationCost() / speed);
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

            double executionTime = currentTask.getComputationCost();

            currentTask.actualStartTime = System.nanoTime();

//            while(executionTime > 0) {

            try {
                Thread.sleep((long)executionTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

//                currentTask.executeForTime((int) timeSlice * speed);

//                executionTime -= timeSlice * speed;

//                earliestFinishTime -= timeSlice * speed;
//            }

            currentTask.actualFinishTime = System.nanoTime();

            currentTask.setCompleted(true);

            currentTask = null;
        }
    }

    public String getName() {
        return name;
    }

//    public int getSpeed() {
//        return speed;
//    }

    @Override
    public void run() {
        while (running) {
            execute();
            try {
                Thread.sleep(1); // Prevent busy-waiting
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public void stop() {
        while(running) {
            if (currentTask == null && taskQueue.isEmpty())
                this.running = false;
        }
    }
}
