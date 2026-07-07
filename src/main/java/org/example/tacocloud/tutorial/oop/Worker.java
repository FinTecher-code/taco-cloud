package org.example.tacocloud.tutorial.oop;

import java.util.List;

public interface Worker {
    void work();
}

class WorkerManager implements Worker{
    @Override
    public void work() {
        System.out.println("Manager is working");
    }
}

class WorkerDeveloper implements Worker{
    @Override
    public void work() {
        System.out.println("Developer is working");
    }
}

class WorkerDesigner implements Worker{

    @Override
    public void work() {
        System.out.println("Designer is working");
    }
}

// Company不关心具体是什么员工，你给它Manager它就管人，给Developer它就写代码。
// Spring IoC帮你做的就是new Worker这个过程，你的代码只需要写Company这种业务逻辑。
class Company{
    // 声明用 List（接口），创建用 ArrayList（实现类）
    // 好处：面向接口编程，以后换实现（如 LinkedList）不用改声明
    private List<Worker> staff;

    // 依赖注入！自己不new，外面传进来
    public Company(List<Worker> staff) {
        this.staff = staff;
    }

    public void startWork(){
        for (Worker worker: staff){
            worker.work(); // 多态！每种员工干活方式不同
        }
    }
}

class Main{
    public static void main(String[] args) {
        // 创建员工
        WorkerManager manager = new WorkerManager();
        WorkerDeveloper developer = new WorkerDeveloper();
        WorkerDesigner designer = new WorkerDesigner();

        // 创建公司，List.of() 是 Java 9 引入的快速创建不可变列表的方法。
        Company company = new Company(List.of(manager, developer, designer));
        company.startWork();
    }
}
