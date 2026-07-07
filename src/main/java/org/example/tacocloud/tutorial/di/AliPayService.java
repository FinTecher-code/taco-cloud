package org.example.tacocloud.tutorial.di;

import org.springframework.stereotype.Component;

@Component
public class AliPayService implements PaymentService{
    @Override
    public String pay(double amount) {
        return "支付宝支付：¥" + amount + " 成功 ✅";
    }
}
