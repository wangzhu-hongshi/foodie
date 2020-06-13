package com.wang.mapper;

import com.wang.my.mapper.MyMapper;
import com.wang.pojo.Items;
import com.wang.vo.ItemCommentVo;
import com.wang.vo.SearchItemsVo;
import com.wang.vo.ShopCartVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {
      List<ItemCommentVo> queryItemComments(@Param("paramsMap") Map<String,Object> Map);
      List<SearchItemsVo> searchItems(@Param("paramsMap") Map<String,Object> Map);
      List<SearchItemsVo> searchItemsByThirdCat(@Param("paramsMap") Map<String,Object> Map);
      List<ShopCartVo> queryItemsBySpecIds(@Param("paramsList") List list);
      int decreaseItemSpecStock(@Param("specId") String specId,
                                @Param("pendingCounts") int pendingCounts);


}