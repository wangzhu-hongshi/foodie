package com.wang.service;

import com.wang.pojo.Category;
import com.wang.vo.CategoryVo;
import com.wang.vo.NewItemsVo;

import java.util.List;

public interface CategoryService {
    /**
     * 查看所有一级分类
     * @return
     */
     List<Category> queryAllRooLevelCat();

    /**
     * 根据一级分类id查询子分类信息
     * @param rootCatId
     * @return
     */
    List<CategoryVo> getSubCatList(Integer rootCatId);

    /**
     * 查询首页每个一级分类下六条最新的商品数据
     * @param rootCatId
     * @return
     */
    List<NewItemsVo> getSixNewItemsLazy(Integer rootCatId);
}
