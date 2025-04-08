package com.sky.consumer;

import com.alibaba.fastjson.JSON;
import com.sky.constant.OrderMessageStatus;
import com.sky.dto.OrderMessageDTO;
import com.sky.entity.OrderMessage;
import com.sky.mapper.OrderMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class OrderPaymentConsumer {

    @Autowired
    private OrderMessageMapper orderMessageMapper;

    /**
     * 监听订单支付成功消息，处理发货逻辑
     * @param message
     */
    @RabbitListener(queues = "order.payment.queue")
    public void onOrderPaymentMessage(String message) {
        log.info("接收到订单支付成功消息：{}", message);
        
        try {
            // 1. 解析消息
            OrderMessageDTO orderMessageDTO = JSON.parseObject(message, OrderMessageDTO.class);
            Long messageId = orderMessageDTO.getMessageId();
            
            // 2. 查询消息记录，实现幂等性
            OrderMessage orderMessage = orderMessageMapper.findById(messageId);
            if (orderMessage == null) {
                log.error("消息记录不存在，messageId: {}", messageId);
                return;
            }
            
            // 如果消息已经处理过，直接返回，实现幂等性
            if (OrderMessageStatus.PROCESSED.equals(orderMessage.getStatus())) {
                log.info("消息已处理，无需重复处理，messageId: {}", messageId);
                return;
            }
            
            // 3. 模拟发货处理
            log.info("开始处理订单发货，订单号: {}, 收货人: {}, 地址: {}, 手机号: {}",
                    orderMessageDTO.getNumber(),
                    orderMessageDTO.getConsignee(),
                    orderMessageDTO.getAddress(),
                    orderMessageDTO.getPhone());
            
            // 模拟发货操作
            System.out.println("订单 " + orderMessageDTO.getNumber() + " 已发货！");
            
            // 4. 更新消息状态为已处理
            orderMessage.setStatus(OrderMessageStatus.PROCESSED);
            orderMessage.setUpdateTime(LocalDateTime.now());
            orderMessageMapper.update(orderMessage);
            
            log.info("订单发货处理成功，订单号: {}", orderMessageDTO.getNumber());
            
        } catch (Exception e) {
            log.error("处理订单发货消息失败", e);
            // 异常处理，可以在这里将消息状态更新为失败
            try {
                OrderMessageDTO orderMessageDTO = JSON.parseObject(message, OrderMessageDTO.class);
                OrderMessage orderMessage = orderMessageMapper.findById(orderMessageDTO.getMessageId());
                if (orderMessage != null) {
                    orderMessage.setStatus(OrderMessageStatus.FAILED);
                    orderMessage.setUpdateTime(LocalDateTime.now());
                    orderMessageMapper.update(orderMessage);
                }
            } catch (Exception ex) {
                log.error("更新消息状态失败", ex);
            }
            
            // 抛出异常，让消息重新入队
            throw new RuntimeException("处理订单发货消息失败", e);
        }
    }
}