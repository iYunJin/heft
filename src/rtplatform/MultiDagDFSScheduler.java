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
        for (DAG dag : dags) {
            List<Node> sortedNodes = topologicalSort(dag.getNodes());
            for (Node node : sortedNodes) {
                Processor selectedProcessor = selectProcessor(node);
                selectedProcessor.schedule(node);
//                System.out.println("Scheduled " + node.getName() + " on " + selectedProcessor.getName());
            }
        }

//        for (Processor processor : processors) {
//            processor.stop();
//        }

//        for (Thread thread : processorThreads) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
    }

    public void addDAG(DAG dag) {
        dags.add(dag);
    }
    private void dfs(Node node, Set<Node> visited, Stack<Node> stack) {
        visited.add(node);
        for (Node successor  : node.getSuc()) {
            if (!visited.contains(successor )) {
                dfs(successor , visited, stack);
            }
        }
        stack.push(node);
    }
    private List<Node> topologicalSort(List<Node> nodes) {
        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();

        for (Node node : nodes) {
            if (!visited.contains(node)) {
                dfs(node, visited, stack);
            }
        }

        List<Node> sortedNodes = new ArrayList<>();
        while (!stack.isEmpty()) {
            sortedNodes.add(stack.pop());
        }
        return sortedNodes;
    }

    private Processor selectProcessor(Node node) {
        // Select the processor with the minimum EFT (Earliest Finish Time)
        Processor selectedProcessor = null;
        long minEFT = Long.MAX_VALUE;

        for (Processor processor : processors) {
            long eft = calculateEFTWithCommunicationCost(processor, node);
            if (eft < minEFT) {
                minEFT = eft;
                selectedProcessor = processor;
            }
        }

        return selectedProcessor;
    }

    private long calculateEFTWithCommunicationCost(Processor processor, Node newTask) {
        long eft = processor.calculateEFT(newTask);
//        for (Node pred : newTask.getPred()) {
//            if (!pred.getProcessorName().equals(processor.getName())) {
//                eft += pred.getCommunicationCost(newTask);
//            }
//        }
        return eft;
    }
    public void addProcess(Processor p){
        this.processors.add(p);
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
