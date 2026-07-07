package org.example.tacocloud.tutorial.beanlife;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component  
public class AppCache {

    private String status;

    @PostConstruct      // 初始化：所有依赖注入完了，spring自动调用这个方法
    public void init(){
        this.status = "已加载";
        System.out.println("✅AppCache init: " + status);
    }

    public void setStatus(String status){
        this.status = status;
        System.out.println("✅AppCache setStatus: " + status);
    }
    public String getStatus(){
        System.out.println("✅AppCache getStatus: " + status);
        return status;
    }
    @PreDestroy     // 销毁：所有bean销毁之前，spring自动调用这个方法
    public void cleanup(){
        System.out.println("✅AppCache cleanup!");
    }

}
