package org.example.tacocloud.oop;

interface USB{
    void connect(); // 接口方法默认public abstract
    void disconnect();
}

class Mouse implements USB{
    @Override
    public void connect() {
        System.out.println("鼠标已连接");
    }

    @Override
    public void disconnect() {
        System.out.println("鼠标已断开");
    }
}

class Keyboard implements USB{
    @Override
    public void connect() {
        System.out.println("键盘已连接");
    }

    @Override
    public void disconnect() {
        System.out.println("键盘已断开");
    }
}
class Monitor implements USB{
    @Override
    public void connect() {
        System.out.println("显示器已连接");
    }

    @Override
    public void disconnect() {
        System.out.println("显示器已断开");
    }
}

// 电脑不管插什么USB设备，都一样操作
class Computer{
    public void plugIn(USB device){
        device.connect();
    }
}

//class Main{
//    public static void main(String[] args) {
//        Computer computer = new Computer();
//        Mouse mouse = new Mouse();
//        Keyboard keyboard = new Keyboard();
//        Monitor monitor = new Monitor();
//
//        computer.plugIn(mouse);
//        computer.plugIn(keyboard);
//        computer.plugIn(monitor);
//    }
//}
