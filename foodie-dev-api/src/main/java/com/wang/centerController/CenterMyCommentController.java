package com.wang.centerController;

import com.imooc.enums.YesOrNo;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import com.wang.bo.center.OrderItemsCommentBO;
import com.wang.centerService.MyCommentService;
import com.wang.controller.BaseController;
import com.wang.pojo.Orders;
import com.wang.pojo.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Api(value = "用户中心评价模块", tags = "用户中心评价模块")
@RestController
@RequestMapping("mycomments")
public class CenterMyCommentController extends BaseController {

    @Autowired
    private MyCommentService myCommentService;

    @ApiOperation(value = "查询该订单的订单详情列表", notes = "查询该订单的订单详情列表", httpMethod = "POST")
    @PostMapping("/pending")
    public IMOOCJSONResult userInfo(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {

        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("订单id 或者 用户id 不能为空");
        }
        //校验用户订单关联
        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if (imoocjsonResult.getStatus() != HttpStatus.OK.value()) {
            return imoocjsonResult;
        }
        //判断订单是否评价过了
        Orders order = (Orders) imoocjsonResult.getData();
        if (order.getIsComment() == YesOrNo.YES.type) {
            return IMOOCJSONResult.errorMsg("该笔订单已经评价过了");
        }

        List list = myCommentService.queryPendingComment(orderId);
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public IMOOCJSONResult saveList(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("订单id 或者 用户id 不能为空");
        }
        //校验用户订单关联
        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if (imoocjsonResult.getStatus() != HttpStatus.OK.value()) {
            return imoocjsonResult;
        }
        if(CollectionUtils.isEmpty(commentList)){
            return IMOOCJSONResult.errorMsg("评论内容不能为空");
        }
        myCommentService.saveComments(userId,orderId,commentList);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "查询历史评价", notes = "查询历史评价",httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId",value = "用户id" ,required = true)
            @RequestParam String userId,
            @ApiParam(name = "page",value = "查询第几页" ,required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "一页展示多少条数据" ,required = false)
            @RequestParam Integer pageSize){
        if(userId == null){
            return IMOOCJSONResult.errorMsg("用户id 不能为空");
        }
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=COMMENT_PAGE_SIZE;

        }
        PagedGridResult pagedGridResult = myCommentService.queryMyComments(userId, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }


}
