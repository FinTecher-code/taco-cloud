package org.example.tacocloud.tutorial.beanlife;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private AppCache appCache;      // singleton, 同一个

    @Autowired
    private ShoppingCart cart1;     // prototype, 新实例

    @Autowired
    private ShoppingCart cart2;     // prototype, 新实例

    @GetMapping("/test-bean")
    public String test() {
        // === Singleton 验证 ===
        appCache.setStatus("被修改了！");

        // === Prototype 验证 ===
        cart1.addItem("苹果");
        cart2.addItem("香蕉");
        cart2.addItem("西瓜");    // cart2 是空车，说明跟 cart1 不是同一个对象

        return "✅ 看控制台输出！";
    }
}
