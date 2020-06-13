package com.wang.service;

import com.imooc.utils.PagedGridResult;
import com.wang.pojo.Items;
import com.wang.pojo.ItemsImg;
import com.wang.pojo.ItemsParam;
import com.wang.pojo.ItemsSpec;
import com.wang.vo.CommentLeveCountsVo;
import com.wang.vo.ItemCommentVo;
import com.wang.vo.ShopCartVo;

import java.util.List;

/**
 * 商品
 */
public interface ItemService {
    /**
     * 根据商品Id 查询商品详情
     *
     * @param
     * @return
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品id 查询 商品图片
     *
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id 查询 商品规格
     *
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id 查询 商品参数
     *
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id 查询 商品评价等级的数量
     *
     * @param itemId
     * @return
     */
    CommentLeveCountsVo queryCommentCount(String itemId);

    /**
     * 根据商品id 查询评价
     *
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult queryItemComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 搜索查询 并按照指定排序
     *
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 根据分类id 搜索查询
     *
     * @param
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 当长时间没有登陆或者访问网站  会更新购物车中商品的规格
     * @param specIds
     * @return
     */
    List<ShopCartVo> queryItemsBySpecIds(String specIds);

    /**
     * 根据 规格id 查询 规格对象的具体信息
     * @param specId
     * @return
     */
    ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品id 获取 商品主图url
     * @param itemId
     * @return
     */
    String queryItemMainImgById(String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     */
    void decreaseItemSpecStock(String specId, int buyCounts);

}



