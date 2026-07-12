package org.example.tacocloud.chqs;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TestConcurrent {

    static int a = 0;

    static void foo(String name, CyclicBarrier barrier) {
        try {
            barrier.await(); // 两个线程同时出发
        } catch (Exception e) { }

        if (a <= 0) {
            a++;
        } else {
            a--;
        }
        System.out.println(name + " 输出: " + a);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== 两线程同时起步, 观察所有可能的输出 ===\n");

        for (int round = 0; round < 200; round++) {
            a = 0;
            CyclicBarrier barrier = new CyclicBarrier(2);

            Thread t1 = new Thread(() -> foo("T1", barrier));
            Thread t2 = new Thread(() -> foo("T2", barrier));

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            // 统计出现过的输出组合
            // 注: 由于 System.out 可能乱序, 这里仅做演示
        }
        System.out.println("执行完毕。可能的输出组合: 22, 12, 21, 10, 01, 00");
        System.out.println("理论上 '01' 不可能出现");
    }
}
