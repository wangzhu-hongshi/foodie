package com.wang.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.CommentLevel;
import com.imooc.enums.YesOrNo;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import com.wang.mapper.*;
import com.wang.pojo.*;
import com.wang.service.ItemService;
import com.wang.vo.CommentLeveCountsVo;
import com.wang.vo.ItemCommentVo;
import com.wang.vo.SearchItemsVo;
import com.wang.vo.ShopCartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class ItemServiceImpl extends BaseService implements ItemService{
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        Items items = itemsMapper.selectByPrimaryKey(itemId);
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example=new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        List<ItemsImg> itemsImgs = itemsImgMapper.selectByExample(example);
        return itemsImgs;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example=new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        List<ItemsSpec> itemsSpecs = itemsSpecMapper.selectByExample(example);
        return itemsSpecs;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example=new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        ItemsParam itemsParam = itemsParamMapper.selectOneByExample(example);
        return itemsParam;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLeveCountsVo queryCommentCount(String itemId) {
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts=goodCounts+normalCounts+badCounts;
        CommentLeveCountsVo comments=new CommentLeveCountsVo();
        comments.setGoodCounts(goodCounts);
        comments.setNormalCounts(normalCounts);
        comments.setBadCounts(badCounts);
        comments.setTotalCounts(totalCounts);
        return comments;
    }
    /**
     * 根据商品id 查询评价
     * @param itemId
     * @param level
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryItemComments(String itemId, Integer level,Integer page,Integer pageSize) {
        Map<String,Object> Map =new HashMap<>();
        Map.put("itemId",itemId);
        Map.put("level",level);
        PageHelper.startPage(page, pageSize);
        List<ItemCommentVo> list = itemsMapperCustom.queryItemComments(Map);
        for (ItemCommentVo itemCommentVo : list) {
            itemCommentVo.setNickName(DesensitizationUtil.commonDisplay(itemCommentVo.getNickName()));
        }
        return setterPagedGrid(list,page);
    }

    /**
     * 搜索商品 并按照指定排序  分页 返回的是分页的响应对象
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort,Integer page,Integer pageSize) {
        Map<String,Object> map =new HashMap<>();
        map.put("keywords",keywords);
        map.put("sort",sort);
        //分页插件 进行 统一得sql拦截
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVo> list = itemsMapperCustom.searchItems(map);
        return setterPagedGrid(list,page);
    }

    /**
     * 根据分类id 搜索查询
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String,Object> map=new HashMap<>();
        map.put("catId",catId);
        map.put("sort",sort);
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVo> list = itemsMapperCustom.searchItemsByThirdCat(map);
        return setterPagedGrid(list,page);
    }

    /**
     * 当长时间没有登陆或者访问网站  会更新购物车中商品的规格
     * @param specIds
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopCartVo> queryItemsBySpecIds(String specIds) {
        String[] split = specIds.split(",");
        List<String> list =new ArrayList<>();
        Collections.addAll(list,split);
        List<ShopCartVo> shopCartVos = itemsMapperCustom.queryItemsBySpecIds(list);
        return shopCartVos;
    }

    /**
     * 根据 规格id 查询 规格对象的具体信息
     * @param specId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        ItemsSpec itemsSpec = itemsSpecMapper.selectByPrimaryKey(specId);
        return itemsSpec;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg=new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);

        return result != null? result.getUrl(): "";
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {
        /**
         * 减库会出现 共享数据不一致情况  多并发的情况下
         * 1. synchronized：不推荐使用 效率低下
         * 2.锁数据库  不推荐  导致数据库性能低下
         * 3.分布式锁 zookeeper redis
         */
        //  分布式操作
        //lockUtil.getLock(); ———加锁
        //lockUtil.unLock();-- 解锁

        //使用数据库 乐观锁
        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if(result != 1){
            throw new RuntimeException("订单创建失败,原因：库存不足");
        }

    }

    /**
     *
     * 辅助查询评价的某个等级数量
     * @param itemId
     * @param level
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer getCommentCounts(String itemId,Integer level){
        ItemsComments itemsComments=new ItemsComments();
        itemsComments.setItemId(itemId);
        if(level!=null){
            itemsComments.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(itemsComments);
    }



}
