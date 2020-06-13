package com.wang.service;

import com.wang.bo.AddressBo;
import com.wang.bo.SubmitOrderBO;
import com.wang.pojo.UserAddress;
import com.wang.vo.OrderVO;

import java.util.List;

/**
 * 订单接口
 */
public interface OrderService {
    /**
     * 创建订单
     * @param submitOrderBO
     * @return
     */
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    void updateOrdersStatus(String orderId,Integer orderStatus);

    /**
     * 关闭过期订单
     */
    void closeOrder();

}
