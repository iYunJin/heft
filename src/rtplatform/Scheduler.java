package rtplatform;

import mh_heft.DAG;
import mh_heft.Node;

import java.util.*;

public class Scheduler {
    private HashMap<String, NodeVertex> sortMap = new HashMap<>();
    private int time = 0;

    public Scheduler(DAG dag) {
        if(dag.sortedTasks== null) {
            dag.topologicalSort();
        }

        Iterator<Node> it = dag.sortedTasks.iterator();
        Node node;
        while(it.hasNext()){
            node = it.next();
            for(Node node1 : node.getSuc()){
                NodeVertex mVertex = sortMap.getOrDefault(node.getName(), new NodeVertex(node));
                mVertex.insertTarget(node1);
                sortMap.put(node.getName(), mVertex);
            }
        }

//        moduleDependencies.forEach((name, dependencies) -> {
//            NodeVertex mVertex = sortMap.getOrDefault(name, new NodeVertex(name));
//            dependencies.forEach(mVertex::insertTarget);
//            sortMap.put(name, mVertex);
//        });
    }

    private void DFS() {
        sortMap.values().forEach(this::DFS_VISIT);
//        Iterator<NodeVertex> it = sortMap.values().iterator();
//        NodeVertex temp;
//        while (it.hasNext()) {
//            temp = it.next();
//            DFS_VISIT(temp);
//        }
    }

    private int DFS_VISIT(NodeVertex mVertex) {
        if (NodeVertex.BLACK == mVertex.color) {
            return mVertex.time + 1;
        }
        if (NodeVertex.GRAY == mVertex.color) {
            return 0;
        }
        int level = 0;//初始层数
        mVertex.color = NodeVertex.GRAY; // 染色 标识正在被搜索，用于判断回环
        Iterator<?> it = mVertex.tarID.iterator();
        while (it.hasNext()) {
            //遍历子节点，获得最大层数为当前层数
            level = Math.max(level, DFS_VISIT(sortMap.get(it.next())));
        }
        mVertex.color = NodeVertex.BLACK;// 搜索完毕
        mVertex.time = level;//记录层数
        return level + 1;//上一层层数是当前层数+1
    }

    public List<NodeVertex> sortByTopologicalSorting() {
        DFS();
        List<NodeVertex> lsModuleVertexs = new LinkedList<>();
        Iterator<NodeVertex> it = sortMap.values().iterator();
        while (it.hasNext()) {
            NodeVertex mVertex = (NodeVertex) it.next();
            lsModuleVertexs.add(mVertex);
        }
        // 按深度优先搜索的time排序
        Collections.sort(lsModuleVertexs);

        return lsModuleVertexs;
    }
}
