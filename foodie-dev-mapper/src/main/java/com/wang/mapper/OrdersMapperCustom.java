package com.wang.mapper;

import com.wang.pojo.OrderStatus;
import com.wang.vo.MyOrdersVO;
import com.wang.vo.MySubOrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface OrdersMapperCustom {

     List<MyOrdersVO> queryMyOrders(@Param("paramsMap")Map<String,Object> map);
    MySubOrderItemVO getSubItems(@Param("orderId") String orderId);

    int getMyOrderStatusCounts(@Param("paramsMap") Map<String,Object> map);

    List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String,Object> map);
}