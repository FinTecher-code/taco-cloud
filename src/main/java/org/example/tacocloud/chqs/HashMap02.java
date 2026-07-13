package org.example.tacocloud.chqs;

class Person02 {
    private String name;

    public Person02(String name) {
        this.name = name;
    }
}


public class HashMap02 {
    public static void main(String[] args) {
        // 假设两个对象
        Person02 p1 = new Person02("Alice");   // hashCode = 12345
        Person02 p2 = new Person02("Bob");     // hashCode = 12345（冲突了）

        // 在 HashMap 中：
        // 步骤 1：计算桶下标 index = hash(key) & (n-1)
        // 假设 12345 & 15 = 9，两个 key 都落到桶 9

        // 步骤 2：放入桶 9
        // 桶 9 已有 p1 → 把 p2 追加到 p1 的 next（链表形式）
            }
}
