package com.wang.controller;

import com.imooc.utils.IMOOCJSONResult;
import com.wang.bo.ShopCartBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 购物车
 */
@Api(value = "购物车接口Controller", tags = {"购物车接口相关api"})
@RestController
@RequestMapping("shopcart")
public class ShopCatController {

    @ApiOperation(value = "添加商品购物车",notes = "添加商品到购物车" ,httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestParam String userId,
                               @RequestBody ShopCartBo shopCartBo,
                               HttpServletRequest request,
                               HttpServletResponse response){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("");
        }
        //TODO 登陆情况下 添加商品 同步redis缓存
        System.out.println(shopCartBo);
        return IMOOCJSONResult.ok();

    }
    @ApiOperation(value = "删除购物车中的商品",notes = "删除购物车中的商品" ,httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(@RequestParam String userId,
                               @RequestParam String itemSpecId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)){
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        //TODO 用户在页面上删除商品数据 如果是登陆状态 则同步删除后端的购物车数据
        return IMOOCJSONResult.ok();
    }
}
