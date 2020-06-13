package com.wang.vo;

/**
 * 商户订单
 */
public class MerchantOrderVO {
    private String merchantOrderId; //商户订单号
    private String merchantUserId;  //发起支付的用户
    private Integer amount;         //实际支付的总金额
    private Integer payMethod;      //支付方式 1 微信 2支付宝
    private String returnUrl;       //支付成功后回调的地址

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
