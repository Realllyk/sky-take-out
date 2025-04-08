package com.sky.mapper;

import com.sky.entity.OrderMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderMessageMapper {
    
    /**
     * 插入订单消息
     * @param orderMessage
     */
    @Insert("insert into order_message (order_id, order_number, status, retry_count, create_time) " +
            "values (#{orderId}, #{orderNumber}, #{status}, 0, #{createTime})")
    void insert(OrderMessage orderMessage);
    
    /**
     * 根据状态查询订单消息
     * @param status
     * @return
     */
    @Select("select * from order_message where status = #{status}")
    List<OrderMessage> findByStatus(Integer status);
    
    /**
     * 更新订单消息
     * @param orderMessage
     */
    @Update("update order_message set status = #{status}, retry_count = #{retryCount}, update_time = now() where id = #{id}")
    void update(OrderMessage orderMessage);
    
    /**
     * 根据消息ID查询订单消息
     * @param id
     * @return
     */
    @Select("select * from order_message where id = #{id}")
    OrderMessage findById(Long id);
}