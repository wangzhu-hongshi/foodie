package com.wang.service;

import java.util.List;

public interface CarouselService {
    /**
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    List queryAll(Integer isShow);
}
