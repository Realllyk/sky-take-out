package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private Integer status; // 使用OrderMessageStatus中的常量
    private Integer retryCount; // 重试次数
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}