package org.example.tacocloud.tutorial.di;

import org.springframework.stereotype.Component;

@Component
public class WeChatPayService implements PaymentService{
    @Override
    public String pay(double amount){
        return "微信支付：¥" + amount + " 成功 ✅";
    }
}
