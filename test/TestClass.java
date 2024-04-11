import org.junit.Test;

public class TestClass {
    @Test
    public void test1(){
        DAG dag = new DAG("dag1");
        Task t1 = new Task("a",8);
        Task t2 = new Task("b",7);
        Task t3 = new Task("c",10);
        Task t4 = new Task("d",6);
        Task t5 = new Task("e",9);
        Task t6 = new Task("f",5);
        Task t7 = new Task("g",11);

        dag.addDependency(t1,t2,8);
        dag.addDependency(t1,t3,9);
        dag.addDependency(t2,t4,7);
        dag.addDependency(t2,t5,12);
        dag.addDependency(t3,t6,18);
        dag.addDependency(t4,t7,4);
        dag.addDependency(t5,t7,9);
        dag.addDependency(t6,t7,6);
        t3.isRTTask = true;
        t4.isRTTask = true;
//        System.out.println("Topological Order:");
//        for (Task vertex : sortedVertices) {
//            System.out.print(vertex.name + " ");
//        }
//        System.out.println();
        dag.entry = t1;
        Scheduler scheduler = new Scheduler();
        scheduler.calculateRank(dag);
        scheduler.getCriticalPath(dag);
//        scheduler.calculateDependencyPriority(dag);
//        dag.printGraph();
//
//        scheduler.schedule();

//        dag.calculateRank();
//        dag.printGraph();
    }
}
