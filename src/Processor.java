import java.util.LinkedList;
import java.util.Queue;

/**
 * Processor class to store the name, speed, and cost of a processor.
 */
public class Processor {
    private String name;
    private int speed;
    private int cost;
    private Queue<Node> taskQueue;
    private Node currentTask;

    public Processor() {
        taskQueue = new LinkedList<>();
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
     * 检查处理器是否可用
     *
     * @return 如果处理器可用，返回true，否则返回false
     */
    public boolean isAvailable() {
        // 如果当前没有正在执行的任务，那么处理器就是可用的
        return currentTask == null;
    }


    /**
     * 将任务调度到处理器
     * @param task 要调度的任务
     */
    public void schedule(Node task) {
        // 如果当前没有正在执行的任务，那么立即开始执行这个任务
        if (currentTask == null) {
            currentTask = task;
        } else {
            // 否则，将任务添加到任务队列中，等待执行
            taskQueue.offer(task);
        }
    }

    /**
     * 执行处理器的下一个任务
     */
    public void execute() {
        // 如果当前的任务已经完成，那么开始执行下一个任务
        if (currentTask != null && currentTask.isCompleted()) {
            currentTask = taskQueue.poll();
            if (currentTask != null) {
                currentTask.isScheduled = true;
                currentTask.earliestStartTime = System.currentTimeMillis();
            }
        }else if (currentTask != null) {
            currentTask.execute();
            // TODO: 这里可以添加一些任务执行的逻辑
        }
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCost() {
        return cost;
    }
}
