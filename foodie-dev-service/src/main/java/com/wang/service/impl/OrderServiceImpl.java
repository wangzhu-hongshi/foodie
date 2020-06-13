package com.wang.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.utils.DateUtil;
import com.wang.bo.SubmitOrderBO;
import com.wang.mapper.OrderItemsMapper;
import com.wang.mapper.OrderStatusMapper;
import com.wang.mapper.OrdersMapper;
import com.wang.pojo.*;
import com.wang.service.AddressService;
import com.wang.service.ItemService;
import com.wang.service.OrderService;
import com.wang.vo.MerchantOrderVO;
import com.wang.vo.OrderVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private Sid sid;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;


    /**
     * 创建订单
     * @param submitOrderBO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        Integer payMethod = submitOrderBO.getPayMethod();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String leftMsg = submitOrderBO.getLeftMsg();
        //设置邮费 包邮为0
        Integer postAmount =0;
        //生成订单id
        String orderId = sid.nextShort();

        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);

        //1.新订单数据保存
        Orders newOrder=new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);


        newOrder.setReceiverName(userAddress.getReceiver());
        newOrder.setReceiverMobile(userAddress.getMobile());
        newOrder.setReceiverAddress(userAddress.getProvince()+" "
                +userAddress.getCity()+" "
                +userAddress.getDistrict()+" "
                +userAddress.getDetail());
        //计算 价格
//
        newOrder.setPostAmount(postAmount);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());

        //2.循环根据itemSpecIds保存订单商品详情表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount=0;//商品原价积累、
        Integer realPayAmount=0;//优惠后的实际支付价格累计
        for (String itemSpecId : itemSpecIdArr) {

            //TODO 整合redis后 商品购买的数量重新从redis的购物车中获取
            int buyCount =1;//购买的单品数量

            //根据规格id 查询规格的具体信息 主要获取价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            totalAmount+=itemsSpec.getPriceNormal()*buyCount;
            realPayAmount+=itemsSpec.getPriceDiscount()*buyCount;

            //2.2 根据商品id 获得商品信息以及商品图片
            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            String mainImgURl = itemService.queryItemMainImgById(itemId);

            //2.3 循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            //构建订单详情对象
            OrderItems subOrderItem=new OrderItems();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(items.getItemName());
            subOrderItem.setItemImg(mainImgURl);
            subOrderItem.setBuyCounts(buyCount);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);
            //2.4 保存订单后 扣除商品的库存数量
            itemService.decreaseItemSpecStock(itemSpecId,buyCount);
        }
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);

        //3.保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);//设置订单状态
        waitPayOrderStatus.setCreatedTime(new Date());//创建时间
        orderStatusMapper.insert(waitPayOrderStatus);

        //4.构建商户订单 用于传给支付中心
        MerchantOrderVO merchantOrderVO=new MerchantOrderVO();
        merchantOrderVO.setMerchantOrderId(orderId);
        merchantOrderVO.setMerchantUserId(userId);
        merchantOrderVO.setAmount(realPayAmount+postAmount);
        merchantOrderVO.setPayMethod(payMethod);

        //5.构建自定义订单vo
        OrderVO orderVO=new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrderVO(merchantOrderVO);

        return orderVO;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrdersStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus=new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {
        OrderStatus status=new OrderStatus();
        status.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> os = orderStatusMapper.select(status);
        for (OrderStatus o : os) {
            //获得创建订单的时间
            Date createdTime = o.getCreatedTime();
            //和当前时间做对比
            int days = DateUtil.daysBetween(createdTime, new Date());
            //大于一天的 关闭订单
            if(days>=1){
                doCloseOrder(o.getOrderId());
            }

        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId){
        OrderStatus closeStatus=new OrderStatus();
        closeStatus.setOrderId(orderId);
        closeStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        closeStatus.setDeliverTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(closeStatus);
    }
}
