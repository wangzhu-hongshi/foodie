package com.wang.mapper;

import com.wang.vo.CategoryVo;
import com.wang.vo.NewItemsVo;

import java.util.List;


public interface CategoryMapperCustom {
    public List<CategoryVo> getSubCatList(Integer rootCatId);

    public List<NewItemsVo> getSixNewItemsLazy(Integer rootCatId);
}