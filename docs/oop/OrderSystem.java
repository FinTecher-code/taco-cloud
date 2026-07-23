package org.example.tacocloud.oop;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

// 接口都不用加public
interface Dish{
    String getName();
    double getPrice();
}
interface Order{
    void addDish(Dish d);
    double getTotalPrice();
}
interface Payment{
    boolean pay(double amount);
}
interface Restaurant{
    void takeOrder(Order o);
}

@Getter
class Noodle implements Dish{
    private String name = "面条";
    private double price = 10;

    public Noodle(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

// 新增打折订单
class DiscountOrderImpl implements Order{
    private List<Dish> dishes = new ArrayList<>();
    @Override
    public void addDish(Dish d) {
        dishes.add(d);
    }
    @Override
    public double getTotalPrice(){
        int nums = dishes.size();
        // 菜品越多，折扣越低（付得越少）：0.9^1=0.9, 0.9^2=0.81, 0.9^3=0.729 ...
        double discount = Math.pow(0.9, nums);
        double totalPrice = 0;
        // 加入final防止循环体内意外引用dish
        for (final Dish dish : dishes){
            totalPrice += dish.getPrice() * discount;
        }
        return totalPrice;
    }
}

class OrderImpl implements Order{

    private List<Dish> dishes = new ArrayList<>();
    @Override
    public void addDish(Dish d) {
        dishes.add(d);
    }
    @Override
    public double getTotalPrice() {
        double totalPrice = 0;
        for (final Dish dish : dishes) {
            totalPrice += dish.getPrice();
        }
        return totalPrice;
    }
}

@Getter
class Rice implements Dish{
    private String name = "米饭";
    private double price = 5;
    public Rice(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

class RestaurantImpl implements Restaurant{
    @Override
    public void takeOrder(Order o) {
        System.out.println("订单已接收" + o.getTotalPrice());
    }
}

class Cashier implements Payment{
    @Override
    public boolean pay(double amount) {
        return true;
    }
}

class WechatPay implements Payment{
    @Override
    public boolean pay(double amount) {
        return true;
    }
}


public class OrderSystem {
    public static void main(String[] args) {
        Restaurant r = new RestaurantImpl();
        Order o = new OrderImpl();
        Payment p = new Cashier();
        o.addDish(new Noodle("面条", 12));
        o.addDish(new Rice("米饭", 6));
        r.takeOrder(o);
        boolean paid = p.pay(o.getTotalPrice());
        System.out.println("支付" + (paid ? "成功" : "失败") + "，总价为" + o.getTotalPrice());
    }
}
