import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Processor class to store the name, speed, and cost of a processor.
 */
public class Processor {
    private String name;
    private int cost;
    Queue<Node> taskQueue;
    private Node currentTask;
    int speed;
    long now_time;
    public Processor() {
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
//            taskQueue.
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
        taskQueue.offer(task);
        task.setProcessor(this);
//        eft += task.computationCost;
//        if (!taskQueue.isEmpty()) {
//            eft += task.communicationCost;
//        }
    }

    /**
     * 执行处理器的下一个任务
     */
    public void execute() {
        if(currentTask == null && taskQueue.isEmpty()) {
            return;
        }
        // 如果当前没有正在执行的任务，那么从任务队列中取出一个任务开始执行
        if(currentTask == null || currentTask.isCompleted()) {

            currentTask = taskQueue.poll();

            if (currentTask != null) {
                currentTask.isScheduled = true;
                currentTask.actualStartTime = System.currentTimeMillis();
//            System.out.println(Thread.currentThread().getName()+ " is tasking ");
                currentTask.execute();
                currentTask.actualFinishTime = System.currentTimeMillis();
            }
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
