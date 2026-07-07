package org.example.tacocloud.tutorial.beanlife;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")     // 每次获取都是新对象，区分默认的单例模式（大家共享一个对象）
public class ShoppingCart {
    private java.util.List<String> items = new java.util.ArrayList<>();

    public void addItem(String item){
        items.add(item);
        System.out.println("🛒 加了商品: " + item + "，当前购物车: " + items);
    }

    public List<String> getItems() {
        return items;
    }
}
