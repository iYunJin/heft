import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Thread.sleep;

public class TestTaskScheduling {

    public static void main(String[] args) throws InterruptedException {
        // 创建三个处理器，每个处理器的处理速度不同
        Processor processor1 = new Processor("Processor1");
        processor1.speed = 1;
        Processor processor2 = new Processor("Processor2");
        processor2.speed = 2;
        Processor processor3 = new Processor("Processor3");
        processor3.speed = 3;

        // 创建一个多DAG调度器
        MultiDagScheduler multiDagScheduler = new MultiDagScheduler();
        multiDagScheduler.addProcessor(processor1);
        multiDagScheduler.addProcessor(processor2);
        multiDagScheduler.addProcessor(processor3);


        Task task1 = new Task() {
            @Override
            public void run(){
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // 创建一个DAG，并添加到多DAG调度器中
        DAG dag = new DAG("dag1");
        Node t1 = new Node("a1",8,task1);
        Node t2 = new Node("a2",7,task1);
        Node t3 = new Node("a3",10,task1);
        Node t4 = new Node("a4",6,task1);
        Node t5 = new Node("a5",9,task1);
        Node t6 = new Node("a6",5,task1);
        Node t7 = new Node("a7",11,task1);
        Node t8 = new Node("a8",5,task1);
        Node t9 = new Node("a9",12,task1);
        Node t10 = new Node("a10",7,task1);
        Node t11 = new Node("a11",4,task1);

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
        Node v1 = new Node("b1",8,task1);
        Node v2 = new Node("b2",7,task1);
        Node v3 = new Node("b3",10,task1);
        Node v4 = new Node("b4",6,task1);
        Node v5 = new Node("b5",9,task1);
        Node v6 = new Node("b6",5,task1);
        Node v7 = new Node("b7",11,task1);

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

//        for(Node task: multiDagScheduler.ready_queue){
//            System.out.println(task.getName());
//        }
//        final long start = System.currentTimeMillis();
        // 创建三个线程，每个线程模拟一个处理器
        Thread thread1 = new Thread(() -> {
            while (!multiDagScheduler.rt_ready_queue.isEmpty() ||!multiDagScheduler.cm_ready_queue.isEmpty()|| !processor1.taskQueue.isEmpty()) {
//                long end = System.currentTimeMillis() - start;
//                System.out.println("processor1 start "+end);
                processor1.execute();
                try {
                    sleep(1); // 等待1m
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            while (!multiDagScheduler.rt_ready_queue.isEmpty() ||!multiDagScheduler.cm_ready_queue.isEmpty()|| !processor2.taskQueue.isEmpty()) {
//                long end = System.currentTimeMillis() - start;
//                System.out.println("processor1 start "+end);
                processor2.execute();
                try {
                    sleep(1); // 等待1m
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            while (!multiDagScheduler.rt_ready_queue.isEmpty() ||!multiDagScheduler.cm_ready_queue.isEmpty()|| !processor3.taskQueue.isEmpty()) {
//                long end = System.currentTimeMillis() - start;
//                System.out.println("processor1 start "+end);
                processor3.execute();
                try {
                    sleep(1); // 等待1m
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        // 启动线程
        thread1.start();
        thread2.start();
        thread3.start();

        dag.getTime();
        dag2.getTime();
        // 主线程继续调度任务

        multiDagScheduler.scheduleTasks();
//        multiDagScheduler.cm_task_in_queue();
//        multiDagScheduler.scheduleTasks();

        // 等待所有线程完成
        thread1.join();
        thread2.join();
        thread3.join();

        // 打印调度结果
        dag.printGraph();
        dag2.printGraph();
    }
}