package com.wang.mapper;

import com.wang.my.mapper.MyMapper;
import com.wang.pojo.ItemsComments;
import com.wang.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    void saveComments( Map<String,Object> map);

    List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String,Object> map);
}