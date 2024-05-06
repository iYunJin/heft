package mh_heft;

import java.util.*;

class NewTask {
    private int executionTime;
    private int priority;
    private int communicationCost;
    public NewTask(int executionTime, int communicationCost) {
        this.executionTime = executionTime;
        this.communicationCost = communicationCost;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public int getPriority() {
        return priority;
    }
    public int getCommunicationCost() {
        return communicationCost;
    }

    public void calculatePriority(List<NewTask> tasks) {
        int totalExecutionTime = 0;
        for (NewTask NewTask : tasks) {
            totalExecutionTime += NewTask.getExecutionTime();
        }
        this.priority = totalExecutionTime;
    }
}

class NewProcessor {
    private List<NewTask> taskQueue = new ArrayList<>();

    public void schedule(NewTask task) {
        taskQueue.add(task);
    }

    public int calculateEFT(NewTask task) {
        int totalExecutionTime = 0;
        for (NewTask t : taskQueue) {
            totalExecutionTime += t.getExecutionTime() + t.getCommunicationCost();
        }
        return totalExecutionTime + task.getExecutionTime() + task.getCommunicationCost();
    }
}

public class HEFTScheduler {
    private List<NewTask> tasks;
    private List<NewProcessor> processors;

    public HEFTScheduler(List<NewTask> tasks, List<NewProcessor> processors) {
        this.tasks = tasks;
        this.processors = processors;
    }

    public void schedule() {
        // Calculate task priorities
        for (NewTask task : tasks) {
            task.calculatePriority(tasks);
        }

        // Sort tasks by priority
        tasks.sort(Comparator.comparing(NewTask::getPriority).reversed());

        // Schedule tasks
        for (NewTask task : tasks) {
            NewProcessor bestProcessor = null;
            int earliestFinishTime = Integer.MAX_VALUE;

            // Find the processor that can finish the task the earliest
            for (NewProcessor processor : processors) {
                int finishTime = processor.calculateEFT(task);
                if (finishTime < earliestFinishTime) {
                    earliestFinishTime = finishTime;
                    bestProcessor = processor;
                }
            }

            // Schedule the task on the best processor
            if (bestProcessor != null) {
                bestProcessor.schedule(task);
            }
        }
    }
}