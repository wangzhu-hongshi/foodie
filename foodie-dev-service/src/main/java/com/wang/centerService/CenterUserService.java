package com.wang.centerService;


import com.wang.bo.UsersBO;
import com.wang.bo.center.CenterUserBO;
import com.wang.pojo.Users;

/**
 * 用户中心
 */
public interface CenterUserService {
     /**
      * 根据用户id 查询 用户信息
      * @param userId
      * @return
      */
     Users queryUserInfo(String userId);

     /**\
      * 更新用户信息
      * @param userId
      * @param centerUserBO
      * @return
      */
     Users updateUserInfo(String userId, CenterUserBO centerUserBO);

     /**
      * 更新用户头像
      * @param userId
      * @param faceUrl
      * @return
      */
     Users updateUserFace(String userId, String faceUrl);
}
