package rtplatform;

import mh_heft.DAG;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Scheduler {
    private HashMap<String, NodeVertex> sortMap = new HashMap<>();
    private int time = 0;

    public Scheduler(DAG dag) {
        if(dag.sortedTasks== null) {
            dag.topologicalSort();
        }


//        moduleDependencies.forEach((name, dependencies) -> {
//            NodeVertex mVertex = sortMap.getOrDefault(name, new NodeVertex(name));
//            dependencies.forEach(mVertex::insertTarget);
//            sortMap.put(name, mVertex);
//        });
    }

    private void DFS() {
        sortMap.values().forEach(this::DFS_VISIT);
    }

    private int DFS_VISIT(NodeVertex mVertex) {
        if (NodeVertex.BLACK == mVertex.color) {
            return mVertex.time + 1;
        }
        if (NodeVertex.GRAY == mVertex.color) {
            return 0;
        }
        mVertex.color = NodeVertex.GRAY;
        int level = mVertex.tarID.stream().mapToInt(id -> DFS_VISIT(sortMap.get(id))).max().orElse(0);
        mVertex.color = NodeVertex.BLACK;
        mVertex.time = level;
        return level + 1;
    }

    public List<NodeVertex> sortByTopologicalSorting() {
        DFS();
        List<NodeVertex> lsModuleVertexs = new ArrayList<>(sortMap.values());
        lsModuleVertexs.sort(Comparator.naturalOrder());
        return lsModuleVertexs;
    }
}
