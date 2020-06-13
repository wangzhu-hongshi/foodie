package com.wang.controller;

import com.imooc.enums.YesOrNo;
import com.imooc.utils.IMOOCJSONResult;
import com.wang.mapper.CategoryMapper;
import com.wang.pojo.Category;
import com.wang.service.CarouselService;
import com.wang.service.CategoryService;
import com.wang.service.impl.CarouselServiceImpl;
import com.wang.vo.CategoryVo;
import com.wang.vo.NewItemsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页
 */
@Api(value = "首页", tags = {"首页展示相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取首页轮播图", notes = "获取首页轮播图",httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel(){
        List carousels = carouselService.queryAll(YesOrNo.YES.type);
        return IMOOCJSONResult.ok(carousels);
    }

    @ApiOperation(value = "查看所有一级分类", notes = "查看所有一级分类",httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult cats(){
        List<Category> categories = categoryService.queryAllRooLevelCat();
        return IMOOCJSONResult.ok(categories);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类",httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId",value = "一级分类Id",required = true)
            @PathVariable Integer rootCatId){
        if(rootCatId == null){
            IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVo> list = categoryService.getSubCatList(rootCatId);
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "查看每个一级分类的最新6个商品数据", notes = "查看每个一级分类的最新6个商品数据",httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "rootCatId",value = "一级分类Id",required = true)
            @PathVariable Integer rootCatId){
        if(rootCatId == null){
            IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVo> list = categoryService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(list);
    }

}
