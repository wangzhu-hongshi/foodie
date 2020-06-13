package com.wang.service.impl;

import com.wang.mapper.CarouselMapper;
import com.wang.pojo.Carousel;
import com.wang.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService{
    @Autowired
    private CarouselMapper carouselMapper;

    /**
     * 查看所有轮播图
     * @param isShow
     * @return
     */
    @Override
    public List queryAll(Integer isShow) {
        Example example =new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow",isShow);
        List<Carousel> carousels = carouselMapper.selectByExample(example);
        return carousels;
    }
}
