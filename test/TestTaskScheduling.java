import java.util.LinkedList;
import java.util.Queue;

public class TestTaskScheduling {

    public static void main(String[] args) throws InterruptedException {
        // 创建三个处理器，每个处理器的处理速度不同
        Processor processor1 = new Processor();
        processor1.speed = 1;
        Processor processor2 = new Processor();
        processor2.speed = 2;
        Processor processor3 = new Processor();
        processor3.speed = 3;

        // 创建一个多DAG调度器
        MultiDagScheduler multiDagScheduler = new MultiDagScheduler();
        multiDagScheduler.addProcessor(processor1);
        multiDagScheduler.addProcessor(processor2);
        multiDagScheduler.addProcessor(processor3);

        // 创建一个DAG，并添加到多DAG调度器中
        DAG dag = new DAG("dag1");
        Node t1 = new Node("t1",8,null);
        Node t2 = new Node("t2",7,null);
        Node t3 = new Node("t3",10,null);
        Node t4 = new Node("t4",6,null);
        Node t5 = new Node("t5",9,null);
        Node t6 = new Node("t6",5,null);
        Node t7 = new Node("t7",11,null);
        Node t8 = new Node("t8",5,null);
        Node t9 = new Node("t9",12,null);
        Node t10 = new Node("t10",7,null);
        Node t11 = new Node("t11",4,null);

        t5.isRTTask = true;
        t7.isRTTask = true;

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

        multiDagScheduler.addDag(dag);

        DAG dag2 = new DAG("dag1");
        Node v1 = new Node("v1",8,null);
        Node v2 = new Node("v2",7,null);
        Node v3 = new Node("v3",10,null);
        Node v4 = new Node("v4",6,null);
        Node v5 = new Node("v5",9,null);
        Node v6 = new Node("v6",5,null);
        Node v7 = new Node("v7",11,null);

        v3.isRTTask = true;
        v4.isRTTask = true;

        dag2.addDependency(v1,v2,10);
        dag2.addDependency(v1,v3,14);
        dag2.addDependency(v2,v4,8);
        dag2.addDependency(v2,v5,9);
        dag2.addDependency(v3,v6,13);
        dag2.addDependency(v4,v7,20);
        dag2.addDependency(v5,v7,25);
        dag2.addDependency(v6,v7,18);

        multiDagScheduler.addDag(dag2);

        //准备工作
        multiDagScheduler.task_job();
        multiDagScheduler.task_in_queue();

        // 创建三个线程，每个线程模拟一个处理器
        Thread thread1 = new Thread(() -> {
            while (!multiDagScheduler.ready_queue.isEmpty() || !processor1.taskQueue.isEmpty()) {
                processor1.execute();
                try {
                    Thread.sleep(1); // 等待1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            while (!multiDagScheduler.ready_queue.isEmpty() || !processor2.taskQueue.isEmpty()) {
                processor2.execute();
                try {
                    Thread.sleep(1); // 等待1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            while (!multiDagScheduler.ready_queue.isEmpty() || !processor3.taskQueue.isEmpty()) {
                processor3.execute();
                try {
                    Thread.sleep(1); // 等待1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 启动线程
        thread1.start();
        thread2.start();
        thread3.start();

        // 主线程继续调度任务
        multiDagScheduler.scheduleTasks();

        // 等待所有线程完成
        thread1.join();
        thread2.join();
        thread3.join();
    }
}