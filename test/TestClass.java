import org.junit.Test;
import mh_heft.*;
import rtplatform.*;

public class TestClass {
    @Test
    public void test1(){
        MultiDagDFSScheduler scheduler = new MultiDagDFSScheduler();
        Processor processor1 = new Processor("Processor1",1);
        Processor processor2 = new Processor("Processor2",2);
        Processor processor3 = new Processor("Processor3",3);

        // Create DAG 1
        DAG dag1 = new DAG("DAG1");
        DAG dag = new DAG("dag1");
        Node t1 = new Node("a1",8,null);
        Node t2 = new Node("a2",7,null);
        Node t3 = new Node("a3",10,null);
        Node t4 = new Node("a4",6,null);
        Node t5 = new Node("a5",9,null);
        Node t6 = new Node("a6",5,null);
        Node t7 = new Node("a7",11,null);
        Node t8 = new Node("a8",5,null);
        Node t9 = new Node("a9",12,null);
        Node t10 = new Node("a10",7,null);
        Node t11 = new Node("a11",4,null);


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
        Node v1 = new Node("b1",8,null);
        Node v2 = new Node("b2",7,null);
        Node v3 = new Node("b3",10,null);
        Node v4 = new Node("b4",6,null);
        Node v5 = new Node("b5",9,null);
        Node v6 = new Node("b6",5,null);
        Node v7 = new Node("b7",11,null);

        dag2.addDependency(v1,v2,10);
        dag2.addDependency(v1,v3,14);
        dag2.addDependency(v2,v4,8);
        dag2.addDependency(v2,v5,9);
        dag2.addDependency(v3,v6,13);
        dag2.addDependency(v4,v7,20);
        dag2.addDependency(v5,v7,25);
        dag2.addDependency(v6,v7,18);

        scheduler.addDAG(dag1);
        scheduler.addDAG(dag2);
        scheduler.addProcess(processor1);
        scheduler.addProcess(processor2);
        scheduler.addProcess(processor3);

        // Schedule nodes
        scheduler.schedule();
    }
}
