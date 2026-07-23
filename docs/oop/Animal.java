package org.example.tacocloud.oop;

abstract class Animal {
    protected String name;

    // 公共实现
    public void eat() {
        System.out.println(name + "is eating");
    }

    // 抽象方法 --> 子类必须自己实现
    public abstract void makeSound();
}

class Dog extends Animal {
    public Dog() {
        this.name = "狗";
    }

    @Override
    public void makeSound() {
        System.out.println("汪汪");
    }
}

// 接口：合同（规定你能干什么）
// 抽象类：半成品（帮你做好一部分，剩下的你自己完成）
