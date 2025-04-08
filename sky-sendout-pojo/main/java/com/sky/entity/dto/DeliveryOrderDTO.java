package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrderDTO implements Serializable {
    private Long id; // 订单id
    private String number; // 订单号
    private Long messageId; // 消息id
    private Long userId; // 用户id
    private String address; // 地址
    private String consignee; // 收货人
    private String phone; // 手机号
    private BigDecimal amount; // 订单金额
    // 其他需要的订单信息
}