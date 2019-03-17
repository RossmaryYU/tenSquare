package com.tensquare.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author yfy
 * @date 2019/3/17
 * 短信监听
 */
@Component
@RabbitListener(queues = "tensquare")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.temp_code}")
    private String tempCode;

    @Value("${aliyun.sms.sign_name}")
    private String signName;

    /**
     * 处理方法
     */
    @RabbitHandler
    public void handlerMsg(Map<String,String> map, Channel channel, Message message){
        System.out.println("手机号:"+map.get("mobile"));
        System.out.println("验证码:"+map.get("code"));

        //使用阿里大于发短信
        try {
            SendSmsResponse sendSmsResponse = smsUtil.sendSms(map.get("mobile"), tempCode, signName, "{\"code\":\"" + map.get("code") + "\"}");
            if(sendSmsResponse.getCode().equals("OK")){
                System.out.println("短信发送成功");

                //手动发确认消息给MQ,让MQ删除队列消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else{
                System.out.println("短信发送失败:"+sendSmsResponse.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
        }
    }
}
