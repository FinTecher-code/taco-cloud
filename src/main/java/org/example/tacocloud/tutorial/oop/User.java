package org.example.tacocloud.tutorial.oop;

public class User {
    private String username;
    private int age;

    // 构造函数
    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }

    // getter setter方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // 行为函数
    public void sayHello() {
        System.out.println("Hello, " + username + "! You are " + age + " years old.");
    }

}

//class Main {
//    public static void main(String[] args) {
//        User user = new User("testUser", 25);
//        User user1 = new User("testUser1", 27);
//        User user2 = new User("testUser2", 30);
//        System.out.println("用户名：" + user.getUsername());
//        System.out.println("年龄：" + user.getAge());
//        System.out.println("用户名：" + user1.getUsername());
//        System.out.println("年龄：" + user1.getAge());
//        System.out.println("用户名：" + user2.getUsername());
//        System.out.println("年龄：" + user2.getAge());
//        user.sayHello();
//        user1.sayHello();
//        user2.sayHello();
//    }
//}
