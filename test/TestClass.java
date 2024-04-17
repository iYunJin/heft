import org.junit.Test;

public class TestClass {
    @Test
    public void test1(){
        DAG dag = new DAG("dag1");
        Task t1 = new Task("a",8);
        Task t2 = new Task("b",19);
        Task t3 = new Task("c",18);
        Task t4 = new Task("d",21);
        Task t5 = new Task("e",7);
        Task t6 = new Task("f",7);
        Task t7 = new Task("g",16);

        dag.addDependency(t1,t2,10);
        dag.addDependency(t1,t3,14);
        dag.addDependency(t2,t4,8);
        dag.addDependency(t2,t5,9);
        dag.addDependency(t3,t6,13);
        dag.addDependency(t4,t7,20);
        dag.addDependency(t5,t7,25);
        dag.addDependency(t6,t7,18);
        t3.isRTTask = true;
        t4.isRTTask = true;
        dag.entry = t1;
        t3.deadline = 64;
        t4.deadline = 59;
        TaskSort ts = new TaskSort();
//        ts.calculateRank(dag);
        ts.DoTaskSort(dag);
        dag.printGraph();

//        DagScheduler dagScheduler = new DagScheduler();
//        dagScheduler.schedule(dag);
//        dag.printGraph();
//        dagScheduler.inQueue();
//        scheduler.calculateDependencyPriority(dag);
//        dag.printGraph();
//
//        scheduler.schedule();

//        dag.calculateRank();
//        dag.printGraph();
    }
}
