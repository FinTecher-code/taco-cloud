package org.example.tacocloud.oop;

import java.util.ArrayList;

// 父类
class Employee {
    protected String name;
    protected double salary;

    public void work(){
        System.out.println("工作中......");
    }
}

// 子类
class Manager extends Employee{
    private double bonus;

    @Override
    public void work(){
        System.out.println("管理团队");
    }
}

class Developer extends Employee{

    @Override
    public void work(){
        System.out.println("写代码");
    }
}

//class Main{
//    public static void main(String[] args) {
//        Employee manager = new Manager();
//        Employee developer = new Developer();
//        ArrayList<Employee> arrayList = new ArrayList<>();
//        arrayList.add(manager);
//        arrayList.add(developer);
//        for (int i = 0; i < arrayList.size(); i++) {
//            arrayList.get(i).work();
//        }
//    }
//}