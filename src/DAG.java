import java.util.*;

public class DAG {
    String name;
    private final Map<Task, List<Task>> graph;
    /**
     * 构造函数
     */
    public DAG(String name){
        this.name = name;
        graph = new HashMap<>();
    }

    /**
     * 添加节点函数
     */
    public void addTask(Task vertex){
        if (!graph.containsKey(vertex)) {
            graph.put(vertex, new ArrayList<>());
        }
    }
    /**
     * 节点之间添加有向边
     * @param fromVertex 出节点
     * @param toVertex 入节点
     */
    public void addDependency(Task fromVertex, Task toVertex, int cost){
        addTask(fromVertex);
        addTask(toVertex);
        fromVertex.addSuc(toVertex);
        toVertex.addPred(fromVertex);
        fromVertex.addCommunicationCost(toVertex,cost);
        graph.get(fromVertex).add(toVertex);
    }

    public List<Task> getTasks(){
        return new ArrayList<>(graph.keySet());
    }

    public List<Task> getDependencies(Task task){
        return graph.get(task);
    }

    /**
     * 拓扑排序
     * @return 排序后的集合
     */
    public List<Task> topologicalSort() {
        Map<Task, Integer> inDegrees = new HashMap<>();

        for (Task task : graph.keySet()) {
            inDegrees.put(task, 0);
        }
        for (List<Task> dependencies : graph.values()) {
            for (Task dependency : dependencies) {
                inDegrees.put(dependency, inDegrees.get(dependency) + 1);
            }
        }

        Queue<Task> queue = new LinkedList<>();
        for (Task task : inDegrees.keySet()) {
            if (inDegrees.get(task) == 0) {
                queue.offer(task);
            }
        }

        List<Task> sortedTasks = new ArrayList<>();
        while (!queue.isEmpty()) {

            Task task = queue.poll();

            sortedTasks.add(task);

            for (Task dependency : graph.get(task)) {
                inDegrees.put(dependency, inDegrees.get(dependency) - 1);

                if (inDegrees.get(dependency) == 0) {
                    queue.offer(dependency);
                }
            }
        }

        if (sortedTasks.size() != graph.size()) {
            throw new IllegalStateException("Graph has a cycle");
        }

        return sortedTasks;
    }
}
