package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo);

    /**
     * 为用户进行历史订单分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult pageQuery4User(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 根据订单id返回订单详情
     * @param id
     * @return
     */
    public OrderVO details(Long id);

    /**
     * 根据id取消订单
     * @param id
     */
    public void userCancelById(Long id) throws Exception;

    /**
     * 根据订单id，将订单菜品详情加入到购物车
     * @param id
     */
    public void repetition(Long id);

    /**
     * 管理端，根据条件分页查询订单信息
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult pageQuery4Admin(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 管理端，统计各个状态的订单数量
     * @return
     */
    public OrderStatisticsVO countStatus();
}
