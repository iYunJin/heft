import java.util.LinkedList;
import java.util.Queue;

public class DagScheduler {
    public Queue<Task> rt_task_priority_queue;
    public Queue<Task> common_task_priority_queue;

//    public DAG dag;

    public DagScheduler(){
        rt_task_priority_queue = new LinkedList<>();
        common_task_priority_queue = new LinkedList<>();
    }

    /**
     * dag节点入列
     */
    public void schedule(DAG dag)
    {
        if(dag.sortedCommonTasks == null | dag.sortedRTTasks ==null)
        {
            new TaskSort().DoTaskSort(dag);
        }
        rt_task_priority_queue.addAll(dag.sortedRTTasks);
        common_task_priority_queue.addAll(dag.sortedCommonTasks);
    }

}
