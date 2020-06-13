package com.wang.centerService;

import com.imooc.utils.PagedGridResult;
import com.wang.bo.center.OrderItemsCommentBO;
import com.wang.vo.MyCommentVO;

import java.util.List;

public interface MyCommentService {
    /**
     * 根据订单id 查询关联商品
     * @param orderId
     * @return
     */
    List queryPendingComment(String orderId);

    /**
     * 保存用户的评论
     * @param userId
     * @param orderId
     * @param commentList
     */
    void saveComments(String userId,String orderId, List<OrderItemsCommentBO> commentList);

    /**
     * 根据用户id 查询历史评价
     * @param userId
     * @return
     */
    PagedGridResult queryMyComments(String userId,Integer page,Integer pageSize);

}
