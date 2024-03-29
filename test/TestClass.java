import org.junit.Test;

import java.util.List;

public class TestClass {
    @Test
    public void test1(){
        DAG dag = new DAG();
        Task t1 = new Task("a",12);
        Task t2 = new Task("b",9);
        Task t3 = new Task("c",7);
        Task t4 = new Task("d",13);
        Task t5 = new Task("e",18);
        Task t6 = new Task("f",15);

        dag.addDependency(t1,t2,8);
        dag.addDependency(t1,t3,12);
        dag.addDependency(t2,t4,12);
        dag.addDependency(t3,t5,10);
        dag.addDependency(t4,t6,10);
        dag.addDependency(t5,t6,6);

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
