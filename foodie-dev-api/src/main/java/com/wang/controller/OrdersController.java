package com.wang.controller;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.wang.bo.SubmitOrderBO;
import com.wang.service.OrderService;
import com.wang.vo.MerchantOrderVO;
import com.wang.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "订单相关接口",tags = "订单相关接口")
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController{
    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;//发起http请求

    @ApiOperation(value ="创建订单",notes = "创建订单",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        Integer payMethod = submitOrderBO.getPayMethod();
        if(payMethod!= PayMethod.WEIXIN.type
        && payMethod!= PayMethod.ALIPAY.type){
            return IMOOCJSONResult.errorMsg("不支持该支付方式");
        }
        /*System.out.println(submitOrderBO);*/
        //1.创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();

        //2.创建订单以后，移除购物车中已结算 已提交的订单商品
        //TODO 整合redis之后 完善购物车中的已结算商品清除 并且同步到前端的cookie
       //  CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);
        //3.向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrderVO merchantOrderVO = orderVO.getMerchantOrderVO();
        merchantOrderVO.setReturnUrl(payReturnUrl);

        //方便测试 把所有的支付金额 改为 1分钱
        //当然也要 校验 小于1分钱的支付金额不能支付
        merchantOrderVO.setAmount(1);

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","imooc");
        headers.add("password","imooc");

        HttpEntity<MerchantOrderVO> entity=new HttpEntity<>(merchantOrderVO,headers);


        ResponseEntity<IMOOCJSONResult> responseEntity=restTemplate.postForEntity(paymentUrl,entity,IMOOCJSONResult.class);

        IMOOCJSONResult paymentResult = responseEntity.getBody();
        if(paymentResult.getStatus()!=200){
            return IMOOCJSONResult.errorMsg("支付中心订单创建失败，请联系管理员!");
        }
        return IMOOCJSONResult.ok(orderId);
    }

    /**
     * 支付成功后 支付中心返回成功的订单号 用于修改订单状态
     * @param merchantOrderId
     * @return
     */
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId){
        orderService.updateOrdersStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

}
