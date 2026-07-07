package org.example.tacocloud.tutorial.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

class EmailService{
    public void send(String msg){
        System.out.println("邮件发送：" + msg);
    }
}

class SmsService{
    public void send(String msg){
        System.out.println("短信发送：" + msg);
    }
}

public class OrderService {
    private EmailService email = new EmailService();
    private SmsService sms = new SmsService();

    public void paySuccess(Long orderId){
        // 硬编码，想改通知方式得改这里
        email.send("订单 " + orderId + " 付款成功");
        sms.send("订单 " + orderId + " 付款成功");
    }
}

class Main{
    public static void main(String[] args) {
        OrderService service = new OrderService();
        service.paySuccess(1L);
    }
}

// 核心问题：想改成只发短信不发邮件？得改OrderService
// 想加个微信通知，又得改OrderService

interface Notifier{
    void send(String message);
}

@Component
class EmailNotifier implements Notifier{

    @Override
    public void send(String message) {
        System.out.println("邮件发送：" + message);
    }
}

@Component
class SmsNotifier implements Notifier{

    @Override
    public void send(String message) {
        System.out.println("短信发送：" + message);
    }
}

@Service
class OrderService2 {
    private final Notifier email;
    private final Notifier sms;

    // 如果只有一个构造器，可以默认不写@Autowired
    @Autowired
    public OrderService2(EmailNotifier email, SmsNotifier sms) {
        this.email = email;
        this.sms = sms;
    }

    public void paySuccess(Long orderId){
        email.send("订单 " + orderId + " 付款成功");
        sms.send("订单 " + orderId + " 付款成功");
    }
}

// 只发短信
@Service
class OrderServiceSms {
    private final Notifier sms;

    // 如果只有一个构造器，可以默认不写@Autowired
    @Autowired
    public OrderServiceSms( SmsNotifier sms) {
        this.sms = sms;
    }

    public void paySuccess(Long orderId){
        sms.send("订单 " + orderId + " 付款成功");
    }
}
