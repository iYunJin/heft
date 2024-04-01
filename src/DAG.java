import java.util.*;

public class DAG {
    String name;
    Task entry;
    private final Map<Task, List<Task>> graph;

    /**
     * 构造函数
     */
    public DAG(String name) {
        this.name = name;
        graph = new HashMap<>();
    }

    /**
     * 添加节点函数
     */
    public void addTask(Task vertex) {
        if (!graph.containsKey(vertex)) {
            graph.put(vertex, new ArrayList<>());
        }
    }

    /**
     * 节点之间添加有向边
     *
     * @param fromVertex 出节点
     * @param toVertex   入节点
     */
    public void addDependency(Task fromVertex, Task toVertex, int cost) {
        addTask(fromVertex);
        addTask(toVertex);
        fromVertex.addSuc(toVertex);
        toVertex.addPred(fromVertex);
        fromVertex.addCommunicationCost(toVertex, cost);
        graph.get(fromVertex).add(toVertex);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(graph.keySet());
    }


    /**
     * 拓扑排序
     * @return 排序后的集合
     */
    public List<Task> topologicalSort() {
        Map<Task, Integer> inDegrees = new HashMap<>();

        //初始化入度
        for (Task task : graph.keySet()) {
            inDegrees.put(task, 0);
        }
        //计算入度
        for (List<Task> dependencies : graph.values()) {
            for (Task dependency : dependencies) {
                inDegrees.put(dependency, inDegrees.get(dependency) + 1);
            }
        }

        //入度为0的节点入队
        Queue<Task> queue = new LinkedList<>();
        for (Task task : inDegrees.keySet()) {
            if (inDegrees.get(task) == 0) {
                queue.offer(task);
            }
        }

        //拓扑排序
        List<Task> sortedTasks = new ArrayList<>();
        while (!queue.isEmpty()) {
            Task task = queue.poll();
            sortedTasks.add(task);
            //更新入度
            for (Task dependency : graph.get(task)) {
                inDegrees.put(dependency, inDegrees.get(dependency) - 1);
                if (inDegrees.get(dependency) == 0) {
                    queue.offer(dependency);
                }
            }
        }

        //判断是否有环
        if (sortedTasks.size() != graph.size()) {
            throw new IllegalStateException("Graph has a cycle");
        }

        return sortedTasks;
    }

    /**
     * 计算依赖优先级,并将其存储在Task类的dependencyPriority中
     * 依赖优先级 = 前驱节点的依赖优先级最大值
     */
    public void calculateDependencyPriority() {
        List<Task> sortedTasks = topologicalSort();
        for (Task task : sortedTasks) {
            int maxPriority = 0;
            for (Task pred : task.pred) {
                maxPriority = Math.max(maxPriority, pred.dependencyPriority);
            }
            task.dependencyPriority = maxPriority;
        }
    }

    public void calculateRank() {
        List<Task> sortedTasks = topologicalSort();
        Collections.reverse(sortedTasks);
        //递归调用计算
        for (Task task : sortedTasks) {
            task.rank = upwardRank(task);
        }
    }
    private double upwardRank(Task task) {
        if (task.suc.isEmpty()) {
            return task.computationCost;
        }
        double maxRank = 0;
        for (Task suc : task.suc) {
            double rank = upwardRank(suc) + task.communicationCosts.get(suc); //到时候加除以处理器的speed
            maxRank = Math.max(maxRank, rank);
        }
        return maxRank + task.computationCost;
    }

    /**
     * 遍历图,打印rank值
     */
    public void printGraph() {
        for (Task task : graph.keySet()){
            System.out.println(task.name+" rank = "+task.rank);
        }
    }

    public void setEntry(Task entry) {
        this.entry = entry;
    }
}