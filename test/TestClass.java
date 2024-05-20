import org.junit.Test;
import mh_heft.*;
import rtplatform.*;

public class TestClass {
    @Test
    public void test1() throws InterruptedException {
        MultiDagDFSScheduler scheduler = new MultiDagDFSScheduler();
        Processor processor1 = new Processor("Processor1",1);
        Processor processor2 = new Processor("Processor2",2);
        Processor processor3 = new Processor("Processor3",3);

        // Create DAG 1

        DAG dag = new DAG("dag1");
        Node t1 = new Node("a1",18,null);
        Node t2 = new Node("a2",17,null);
        Node t3 = new Node("a3",20,null);
        Node t4 = new Node("a4",16,null);
        Node t5 = new Node("a5",19,null);
        Node t6 = new Node("a6",15,null);
        Node t7 = new Node("a7",21,null);
        Node t8 = new Node("a8",15,null);
        Node t9 = new Node("a9",22,null);
        Node t10 = new Node("a10",17,null);
        Node t11 = new Node("a11",14,null);


        dag.addDependency(t1,t2,17);
        dag.addDependency(t1,t3,12);
        dag.addDependency(t2,t4,11);
        dag.addDependency(t2,t5,20);
        dag.addDependency(t3,t6,15);
        dag.addDependency(t6,t7,8);
        dag.addDependency(t6,t8,13);
        dag.addDependency(t4,t11,14);
        dag.addDependency(t5,t11,9);
        dag.addDependency(t7,t9,12);
        dag.addDependency(t8,t9,5);
        dag.addDependency(t9,t10,10);
        dag.addDependency(t10,t11,13);

        // Create DAG 2
        DAG dag2 = new DAG("DAG2");
        Node v1 = new Node("b1",18,null);
        Node v2 = new Node("b2",17,null);
        Node v3 = new Node("b3",20,null);
        Node v4 = new Node("b4",16,null);
        Node v5 = new Node("b5",19,null);
        Node v6 = new Node("b6",15,null);
        Node v7 = new Node("b7",21,null);

        dag2.addDependency(v1,v2,10);
        dag2.addDependency(v1,v3,14);
        dag2.addDependency(v2,v4,8);
        dag2.addDependency(v2,v5,9);
        dag2.addDependency(v3,v6,13);
        dag2.addDependency(v4,v7,20);
        dag2.addDependency(v5,v7,25);
        dag2.addDependency(v6,v7,18);

        scheduler.addDAG(dag);
        scheduler.addDAG(dag2);
        scheduler.addProcess(processor1);
        scheduler.addProcess(processor2);
        scheduler.addProcess(processor3);

        // 创建三个线程，每个线程模拟一个处理器
        Thread thread1 = new Thread(processor1);
        Thread thread2 = new Thread(processor2);
        Thread thread3 = new Thread(processor3);


        // 启动线程
        thread1.start();
        thread2.start();
        thread3.start();

        dag.getTime();
        dag2.getTime();
//        dag.topologicalSort();
//        dag.printGraph();
        // Schedule nodes
        scheduler.schedule();

        processor1.stop();
        processor2.stop();
        processor3.stop();
        thread1.join();
        thread2.join();
        thread3.join();

        dag.printGraph();
        dag2.printGraph();
    }
}
