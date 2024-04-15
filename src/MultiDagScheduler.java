import java.util.ArrayList;
import java.util.List;

public class MultiDagScheduler{
    private List<DagScheduler> schedulers;
    private List<DAG> dags;

    public MultiDagScheduler() {
        schedulers = new ArrayList<>();
    }

    public void addDag(DAG dag) {
        dags.add(dag);
    }

    /**
     * dag节点入列
     */
    public void inQueue() {
        for (DAG dag : dags) {
            DagScheduler scheduler = new DagScheduler();
            schedulers.add(scheduler);
            scheduler.schedule(dag);
//            scheduler.inQueue(dag);
//            schedulers.add(scheduler);
        }
    }
}