package rtplatform;

import mh_heft.DAG;
import mh_heft.Node;
import mh_heft.Processor;

import java.util.*;

public class MultiDagDFSScheduler {
    private List<DAG> dags;
    private List<Processor> processors;
    private List<Thread> processorThreads;
    public MultiDagDFSScheduler() {
        this.dags = new ArrayList<>();
        this.processors = new ArrayList<>();
        this.processorThreads = new ArrayList<>();
    }

    public void schedule() {

        Node readytask = null;
        List<Node> allTasks = new ArrayList<>();
        for (DAG dag : dags) {
            dag.topologicalSort();
            allTasks.addAll(dag.sortedTasks);
        }

        // Compute ranku for each task
        for (Node task : allTasks) {
            task.rank = computeRanku(task);

            task.setDeadline(calculateDeadline(task));
            if(task.priorityLevel == Node.HIGH_PRIORITY)
                task.deadline *= 1.1;
            else if(task.priorityLevel == Node.MEDIUM_PRIORITY)
                task.deadline *= 1.2;
            else
                task.deadline *= 1.3;
        }

        // Sort tasks by decreasing ranku
        allTasks.sort((a, b) -> Double.compare(b.rank, a.rank));

        for (Node asd : allTasks) {
            System.out.println(asd.getName());
        }

        for(DAG dag:dags)
            dag.getTime();

        while (!allTasks.isEmpty()) {

            if(readytask == null || readytask.isCompleted()) {
                readytask = allTasks.get(0);
                Processor minEftProcessor = selectProcessor(readytask);
                minEftProcessor.schedule(readytask);
                allTasks.remove(readytask);

                scheduleRemainingTasks(allTasks.iterator());
                scheduleRemainingTasks(allTasks.iterator());

            }
        }

    }
    private void scheduleRemainingTasks(Iterator<Node> iterator) {
//        int count = 0;
//        Iterator<Node> iterator = ready_queue.iterator();
        while (iterator.hasNext()) {
            Node task = iterator.next();
            if (!task.allDependenciesCompleted()) {
                continue;
            }
            Processor minEftProcessor = selectProcessor(task);
            if (minEftProcessor != null) {
//                task.slack = System.currentTimeMillis();
                minEftProcessor.schedule(task);
                iterator.remove();
                break;
            }
        }
    }

    private double computeRanku(Node task) {
        if (task.getSuc().isEmpty()) {
            return task.getComputationCost();
        }

        double maxRank = 0;
        for (Node succ : task.getSuc()) {
//            double commCost = ;
            double rank = computeRanku(succ) + task.getCommunicationCost(succ) + task.getComputationCost();
            if (rank > maxRank) {
                maxRank = rank;
            }
        }
        return maxRank;
    }

    public void addDAG(DAG dag) {
        dags.add(dag);
    }


    private void scheduleTask(Node task) {

        Processor minEftProcessor = selectProcessor(task);
        if (minEftProcessor != null) {
                    // 将任务调度到处理器
            task.slack = System.currentTimeMillis();
            minEftProcessor.schedule(task);
        }
    }


    private Processor selectProcessor(Node node) {
        // Select the processor with the minimum EFT (Earliest Finish Time)
        Processor selectedProcessor = null;
        double minEFT = Long.MAX_VALUE;

        for (Processor processor : processors) {
            double eft = calculateEFTWithCommunicationCost(processor, node);
            if (eft < minEFT) {
                minEFT = eft;
                selectedProcessor = processor;
            }
        }

        return selectedProcessor;
    }

    private double calculateEFTWithCommunicationCost(Processor processor, Node newTask) {
        double eft = processor.calculateEFT(newTask);
        for (Node pred : newTask.getPred()) {
            if (!pred.getProcessorName().equals(processor.getName())) {
                eft += pred.getCommunicationCost(newTask);
            }
        }
        return eft;
    }
    public void addProcess(Processor p){
        this.processors.add(p);
    }
    private int calculateDeadline(Node task){
        if(task.pred.isEmpty()){
            return 0;
        }
        int maxDead = 0;
        for(Node pred : task.pred){
            int deadline  = calculateDeadline(pred) + pred.communicationCosts.get(task);
            maxDead = Math.max(maxDead,deadline);
        }
        return maxDead + task.computationCost;
    }
//    private void scheduleTask(Node node, Map<Node, Long> nodeStartTimes, Map<Node, Long> nodeEndTimes) {
//        long earliestStart = 0;
//        for (Node dependency : node.getPred()) {
//            if (!nodeEndTimes.containsKey(dependency)) {
//                throw new IllegalStateException("Dependency not scheduled: " + dependency.getName());
//            }
//            earliestStart = Math.max(earliestStart, nodeEndTimes.get(dependency));
//        }
//
//        Processor assignedProcessor = null;
//        for (Processor processor : processors) {
//            if (processor.isIdle() || processor.currentTime <= earliestStart) {
//                assignedProcessor = processor;
//                break;
//            }
//        }
//
//        if (assignedProcessor == null) {
//            assignedProcessor = processors.get(0);
//            for (Processor processor : processors) {
//                if (processor.currentTime < assignedProcessor.currentTime) {
//                    assignedProcessor = processor;
//                }
//            }
//            earliestStart = assignedProcessor.currentTime;
//        }
//
//        assignedProcessor.assignTask(node, earliestStart);
//        nodeStartTimes.put(node, earliestStart);
//        nodeEndTimes.put(node, earliestStart + node.getLength());
//        System.out.println("Processor " + assignedProcessor.id + " assigned Node " + node.getName() + " from " + earliestStart + " to " + (earliestStart + node.getLength()));
//        assignedProcessor.completeTask();
//    }


//    private void printSchedule(Map<Task, Integer> taskStartTimes, Map<Task, Integer> taskEndTimes) {
//        int totalWaitingTime = 0;
//        int totalTurnaroundTime = 0;
//        int totalTasks = taskStartTimes.size();
//        int totalCompletionTime = 0;
//
//        for (Task task : taskStartTimes.keySet()) {
//            int waitingTime = taskStartTimes.get(task);
//            int turnaroundTime = taskEndTimes.get(task);
//            totalWaitingTime += waitingTime;
//            totalTurnaroundTime += turnaroundTime;
//            totalCompletionTime = Math.max(totalCompletionTime, taskEndTimes.get(task));
//
//            System.out.println("Task " + task.id + " starts at " + taskStartTimes.get(task) + " and ends at " + taskEndTimes.get(task));
//        }
//
//        System.out.println("Average Waiting Time: " + (float)totalWaitingTime / totalTasks);
//        System.out.println("Average Turnaround Time: " + (float)totalTurnaroundTime / totalTasks);
//        System.out.println("Total Completion Time: " + totalCompletionTime);
//    }
}
