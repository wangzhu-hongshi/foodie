package com.wang.centerService;

import com.imooc.utils.PagedGridResult;
import com.wang.pojo.OrderStatus;
import com.wang.pojo.Orders;
import com.wang.vo.MyOrdersVO;
import com.wang.vo.OrderStatusCountsVO;

import java.util.List;

public interface MyOrdersService {

    /**
     * 查询我的订单 list
     * @param userId
     * @param orderStatus
     * @return
     */
    PagedGridResult queryMyOrders(String userId,Integer orderStatus,Integer page,Integer pageSize);

    /**
     * 商家发货 修改订单状态
     * @param orderId
     */
    void updateDeliverOrderStatus(String orderId);

    /**
     * 根据userId 和orderId 查看是否关联
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId,String orderId);

    /**
     * 确认收货 修改订单状态
     * @param orderId
     * @return
     */
    boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单 （逻辑删除）
     * @param userId
     * @param orderId
     * @return
     */
    boolean deleteOrder(String userId,String orderId);

    /**
     * 查询所有 订单状态的订单数量 一次性查询所有
     * @param userId
     * @return
     */
    OrderStatusCountsVO getOrderStatusCounts(String userId);

    /**
     * 订单动向
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult getMyOrderTrend(String userId,Integer page,Integer pageSize);
}
