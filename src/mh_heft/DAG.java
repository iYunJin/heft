package mh_heft;

import java.util.*;

public class DAG {
    String name;
    Node entry;
    private List<Node> nodes;
    //存储图的邻接表
    public final Map<Node, List<Node>> graph;
    int TaskNum;
    public int rtTaskNum;
    public int commonTaskNum;
    public List<Node> sortedTasks;
    public List<Node> sortedRTTasks;
    public List<Node> sortedCommonTasks;

    long start;

    /**
     * 构造函数
     */
    public DAG(String name) {
        this.name = name;
        graph = new HashMap<>();
        nodes = new ArrayList<>();
    }

    /**
     * 添加节点函数
     */
    public void addTask(Node node) {
        if (!graph.containsKey(node)) {
            if (!node.suc.isEmpty())
                graph.put(node, node.suc);
            else
                graph.put(node, new ArrayList<>());
        }
        if(!nodes.contains((node))){
            nodes.add(node);
        }
    }

    /**
     * 节点之间添加有向边
     *
     * @param fromVertex 出节点
     * @param toVertex   入节点
     */
    public void addDependency(Node fromVertex, Node toVertex, int cost) {
        addTask(fromVertex);
        addTask(toVertex);
        fromVertex.addSuc(toVertex);
        toVertex.addPred(fromVertex);
        fromVertex.addCommunicationCost(toVertex, cost);
        graph.get(fromVertex).add(toVertex);
    }

    public List<Node> getTasks() {
        return new ArrayList<>(graph.keySet());
    }

    /**
     * 拓扑排序
     */
    public void topologicalSort() {
        Map<Node, Integer> inDegrees = new HashMap<>();

        //初始化入度
        for (Node task : graph.keySet()) {
            inDegrees.put(task, 0);
        }

        //计算入度
        for (List<Node> dependencies : graph.values()) {
            for (Node dependency : dependencies) {
                inDegrees.put(dependency, inDegrees.get(dependency) + 1);
            }
        }

        //入度为0的节点入队
        Queue<Node> queue = new LinkedList<>();
        for (Node task : inDegrees.keySet()) {
            if (inDegrees.get(task) == 0) {
                queue.offer(task);
            }
        }

        //拓扑排序
        sortedTasks = new ArrayList<>();
        while (!queue.isEmpty()) {
            Node task = queue.poll();
            sortedTasks.add(task);
            //更新入度
            for (Node dependency : graph.get(task)) {
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
    }


    public void getTime(){
        start = System.currentTimeMillis();
    }
    /**
     * 遍历图,打印rank值
     */
    public void printGraph(){
        if (sortedTasks == null)
            topologicalSort();

        for(Node sortedTask : sortedTasks){

            System.out.println(sortedTask.getName() + " start in "+sortedTask.getProcessorName()+" in " +
                    "time " + (sortedTask.actualStartTime-start));
        }
//            System.out.println(sortedTask.getName() + " dependencyPriority:" + sortedTask.dependencyPriority);

    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public List<Node> getNodes() {
        return nodes;
    }
    public void setEntry(Node entry) {
        this.entry = entry;
    }
}