package com.wang.controller;

import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import com.wang.pojo.Items;
import com.wang.pojo.ItemsImg;
import com.wang.pojo.ItemsParam;
import com.wang.pojo.ItemsSpec;
import com.wang.service.ItemService;
import com.wang.vo.CommentLeveCountsVo;
import com.wang.vo.ItemInfoVo;
import com.wang.vo.ShopCartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品接口",tags = "商品详情展示的相关接口")
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {
    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "商品接口", notes = "商品详情接口",httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult info(
            @ApiParam(name = "itemId",value = "商品ID" ,required = true)
            @PathVariable String itemId){
        if(itemId == null){
            return IMOOCJSONResult.errorMsg("商品ID不能为空");
        }
        ItemInfoVo itemInfoVo=new ItemInfoVo();
        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        itemInfoVo.setItem(items);
        itemInfoVo.setItemImgList(itemsImgList);
        itemInfoVo.setItemSpecList(itemsSpecList);
        itemInfoVo.setItemParams(itemsParam);

        return IMOOCJSONResult.ok(itemInfoVo);
    }

    @ApiOperation(value = "查询商品评价等级数量", notes = "查询商品评价等级数量",httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(
            @ApiParam(name = "itemId",value = "商品ID" ,required = true)
            @RequestParam String itemId){
        if(itemId == null){
            return IMOOCJSONResult.errorMsg("商品ID不能为空");
        }
        CommentLeveCountsVo CountsVo = itemService.queryCommentCount(itemId);
        return IMOOCJSONResult.ok(CountsVo);
    }
    @ApiOperation(value = "查询商品评论", notes = "查询商品评论",httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult comments(
            @ApiParam(name = "itemId",value = "商品ID" ,required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level",value = "评价等级" ,required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page",value = "查询第几页" ,required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "一页展示多少条数据" ,required = false)
            @RequestParam Integer pageSize){
        if(itemId == null){
            return IMOOCJSONResult.errorMsg("商品ID不能为空");
        }
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=COMMENT_PAGE_SIZE;

        }
        PagedGridResult pagedGridResult = itemService.queryItemComments(itemId, level, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }
    @ApiOperation(value = "搜素商品列表", notes = "搜素商品列表",httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult search(
            @ApiParam(name = "keywords",value = "搜索关键字" ,required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort",value = "排序" ,required = false)
            @RequestParam String sort,
            @ApiParam(name = "page",value = "查询第几页" ,required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "一页展示多少条数据" ,required = false)
            @RequestParam Integer pageSize){
        if(keywords == null){
            return IMOOCJSONResult.errorMsg(null);
        }
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=PAGE_SIZE;

        }
        PagedGridResult pagedGridResult = itemService.searchItems(keywords,sort,page,pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "根据分类id搜索商品列表", notes = "根据分类id搜索商品列表",httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult catItems(
            @ApiParam(name = "catId",value = "分类id" ,required = true)
            @RequestParam Integer catId ,
            @ApiParam(name = "sort",value = "排序" ,required = false)
            @RequestParam String sort,
            @ApiParam(name = "page",value = "查询第几页" ,required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "一页展示多少条数据" ,required = false)
            @RequestParam Integer pageSize){
        if(catId == null){
            return IMOOCJSONResult.errorMsg(null);
        }
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=PAGE_SIZE;

        }
        PagedGridResult pagedGridResult = itemService.searchItemsByThirdCat(catId,sort,page,pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }
    @ApiOperation(value = "根据商品规格Ids查询最新的商品数据", notes = "根据商品规格Ids查询最新的商品数据",httpMethod = "GET")
    @GetMapping("/refresh")
    public IMOOCJSONResult refresh(
            @ApiParam(name = "itemSpecIds",value = "itemSpecIds" ,required = true,example = "1001,1002,1003")
            @RequestParam String itemSpecIds){

        if(StringUtils.isBlank(itemSpecIds)){
            return IMOOCJSONResult.ok();
        }
        List<ShopCartVo> list = itemService.queryItemsBySpecIds(itemSpecIds);
        return IMOOCJSONResult.ok(list);
    }
}
