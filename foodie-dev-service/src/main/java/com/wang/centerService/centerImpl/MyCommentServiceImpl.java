package com.wang.centerService.centerImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.utils.PagedGridResult;
import com.wang.bo.center.OrderItemsCommentBO;
import com.wang.centerService.MyCommentService;
import com.wang.centerService.MyOrdersService;
import com.wang.mapper.*;
import com.wang.pojo.ItemsComments;
import com.wang.pojo.OrderItems;
import com.wang.pojo.OrderStatus;
import com.wang.pojo.Orders;
import com.wang.service.impl.BaseService;
import com.wang.vo.MyCommentVO;
import com.wang.vo.MyOrdersVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyCommentServiceImpl extends BaseService implements MyCommentService{
    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private  ItemsCommentsMapperCustom commentsMapperCustom;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List queryPendingComment(String orderId) {
        OrderItems queryOrderItems=new OrderItems();
        queryOrderItems.setOrderId(orderId);
        List<OrderItems> orderItemsListr = orderItemsMapper.select(queryOrderItems);
        return orderItemsListr;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String userId, String orderId, List<OrderItemsCommentBO> commentList) {
        //1.保存评价 items_comments
        for (OrderItemsCommentBO commentBO : commentList) {
            commentBO.setCommentId(sid.nextShort());
        }
        Map<String,Object> map=new HashMap<>();
        map.put("userId",userId);
        map.put("commentList",commentList);
        commentsMapperCustom.saveComments(map);
        //2.修改订单表的为已评价
        Orders order=new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(order);
        //修改订单状态表的留言时间
        OrderStatus orderStatus=new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);

    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId,Integer page,Integer pageSize) {

        Map<String,Object> map=new HashMap<>();
        map.put("userId",userId);
        PageHelper.startPage(page,pageSize);
        List<MyCommentVO> myCommentVOList = commentsMapperCustom.queryMyComments(map);
        PagedGridResult pagedGridResult = setterPagedGrid(myCommentVOList, page);
        return pagedGridResult;
    }



}
