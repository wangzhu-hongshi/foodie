package com.wang.controller;

import com.imooc.utils.IMOOCJSONResult;
import com.wang.centerService.MyOrdersService;
import com.wang.pojo.Orders;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 *
 */
public class BaseController {

    @Autowired
    public MyOrdersService myOrdersService;
    /**
     * 后端默认PageSize
     */
    public static final String FOODIE_SHOPCART ="shopcart";
    public static final Integer COMMENT_PAGE_SIZE=10;
    public static final Integer PAGE_SIZE=20;

    //支付中心的调用地址
    String paymentUrl="http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    //微信支付成功 -> 支付中心 -> 电商平台
     String payReturnUrl="http://localhost:8088/orders/notifyMerchantOrderPaid";

     //用户上传头像的位置
    public static final String IMAGE_USER_FACE_LOCATION="f:"+ File.separator+"workspaces"+
             File.separator+"images"+
             File.separator+ "foodie"+
             File.separator+"faces";
    /**
     * 校验该用户和该订单是否有关联
     * @param userId
     * @param orderId
     * @return
     */
    public IMOOCJSONResult checkUserOrder(String userId, String orderId){
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if(order == null){
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        return IMOOCJSONResult.ok(order);

    }


}
