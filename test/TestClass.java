import org.junit.Test;

import java.util.List;

public class TestClass {
    @Test
    public void test1(){
        DAG dag = new DAG("dag1");
        Task t1 = new Task("a",8);
        Task t2 = new Task("b",14);
        Task t3 = new Task("c",9);
        Task t4 = new Task("d",18);
        Task t5 = new Task("e",18);
        Task t6 = new Task("f",5);

        dag.addDependency(t1,t2,9);
        dag.addDependency(t1,t3,12);
        dag.addDependency(t1,t4,14);
        dag.addDependency(t2,t3,16);
        dag.addDependency(t2,t5,9);
        dag.addDependency(t3,t5,11);
        dag.addDependency(t4,t6,7);
        dag.addDependency(t5,t6,13);

        List<Task> sortedVertices = dag.topologicalSort();
        System.out.println("Topological Order:");
        for (Task vertex : sortedVertices) {
            System.out.print(vertex.name + " ");
        }
        System.out.println();

        HEFTScheduler scheduler = new HEFTScheduler(dag);

        scheduler.schedule();

    }
}
