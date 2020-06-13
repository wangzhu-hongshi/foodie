package com.wang.centerController;

import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import com.wang.centerService.MyOrdersService;
import com.wang.controller.BaseController;
import com.wang.pojo.Orders;
import com.wang.vo.OrderStatusCountsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.asm.IModelFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "用户中心我的订单相关接口", tags = {"用户订单相关接口"})
@RestController
@RequestMapping("myorders")
public class CenterMyOrderController extends BaseController {



    /**
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "根据用户id 和 订单状态查看 订单列表",notes = "根据用户id 和 订单状态查看 订单列表",httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query( @ApiParam(name = "userId",value = "用户id" ,required = true)
                                      @RequestParam String userId,
                                  @ApiParam(name = "orderStatus",value = "订单状态" ,required = false)
                                      @RequestParam Integer orderStatus,
                                  @ApiParam(name = "page",value = "查询第几页" ,required = false)
                                      @RequestParam Integer page,
                                  @ApiParam(name = "pageSize",value = "一页展示多少条数据" ,required = false)
                                      @RequestParam Integer pageSize){
        if(userId == null){
            return IMOOCJSONResult.errorMsg("userId 不能为空");
        }
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=COMMENT_PAGE_SIZE;

        }
        PagedGridResult pagedGridResult = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);

    }
    @ApiOperation(value = "商家发货",notes = "商家发货",httpMethod = "GET")
    @GetMapping("/deliver")
    public IMOOCJSONResult deliver(@ApiParam(name = "orderId",value = "订单id" ,required = true)
                                 @RequestParam String orderId) {
        if(StringUtils.isBlank(orderId)){
            return IMOOCJSONResult.errorMsg("订单ID 不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return IMOOCJSONResult.ok();

    }

    /**
     *
     * @param userId
     * @param orderId
     * @return
     */
    @ApiOperation(value = "用户发起确认收货",notes = "用户发起确认收货",httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public IMOOCJSONResult confirmReceive(@ApiParam(name = "userId",value = "用户Id" ,required = true)
                                       @RequestParam String userId,
                                   @ApiParam(name = "orderId",value = "订单id" ,required = true)
                                       @RequestParam String orderId) {
        if(StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("订单id 或者 用户id 不能为空");
        }
        //校验用户订单关联
        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if(imoocjsonResult.getStatus() != HttpStatus.OK.value()){
            return imoocjsonResult;
        }
        boolean result = myOrdersService.updateReceiveOrderStatus(orderId);
        if(!result){
            return IMOOCJSONResult.errorMsg("确认收货失败！");
        }
        return IMOOCJSONResult.ok();

    }
    @ApiOperation(value = "删除订单（逻辑删除）",notes = "删除订单（逻辑删除）",httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(@ApiParam(name = "userId",value = "用户Id" ,required = true)
                                          @RequestParam String userId,
                                          @ApiParam(name = "orderId",value = "订单id" ,required = true)
                                          @RequestParam String orderId) {
        if(StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("订单id 或者 用户id 不能为空");
        }
        //校验用户订单关联
        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if(imoocjsonResult.getStatus() != HttpStatus.OK.value()){
            return imoocjsonResult;
        }
        boolean result = myOrdersService.deleteOrder(userId,orderId);
        if(!result){
            return IMOOCJSONResult.errorMsg("删除订单失败！");
        }
        return IMOOCJSONResult.ok();

    }
    @ApiOperation(value = "查询所有 订单状态的订单数量",notes = "查询所有 订单状态的订单数量",httpMethod = "POST")
    @PostMapping("/statusCounts")
    public IMOOCJSONResult statusCounts(@ApiParam(name = "userId",value = "用户Id" ,required = true)
                                  @RequestParam String userId) {
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("userId 不能为空");
        }
        OrderStatusCountsVO orderStatusCounts = myOrdersService.getOrderStatusCounts(userId);
        return IMOOCJSONResult.ok(orderStatusCounts);

    }

    @ApiOperation(value = "根据userId查看订单动向",notes = "根据userId查看订单动向",httpMethod = "POST")
    @PostMapping("/trend")
    public IMOOCJSONResult trend( @ApiParam(name = "userId",value = "用户id" ,required = true)
                                  @RequestParam String userId,
                                  @ApiParam(name = "page",value = "查询第几页" ,required = false)
                                  @RequestParam Integer page,
                                  @ApiParam(name = "pageSize",value = "一页展示多少条数据" ,required = false)
                                  @RequestParam Integer pageSize){
        if(userId == null){
            return IMOOCJSONResult.errorMsg("userId 不能为空");
        }
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=COMMENT_PAGE_SIZE;

        }
        PagedGridResult myOrderTrend = myOrdersService.getMyOrderTrend(userId, page, pageSize);

        return IMOOCJSONResult.ok(myOrderTrend);
    }


}
