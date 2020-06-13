package com.wang.centerService.centerImpl;

import com.wang.bo.center.CenterUserBO;
import com.wang.centerService.CenterUserService;
import com.wang.mapper.UsersMapper;
import com.wang.pojo.Users;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService{

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 根据用户id 查询 用户信息
     * @param userId
     * @return
     */
    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;
    }

    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users updateUsers=new Users();
        BeanUtils.copyProperties(centerUserBO,updateUsers);
        updateUsers.setId(userId);
        updateUsers.setUpdatedTime(new Date());//设置更新的时间
        int i = usersMapper.updateByPrimaryKeySelective(updateUsers);

        return queryUserInfo(userId);
    }

    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users updateUsers=new Users();
        updateUsers.setFace(faceUrl);
        updateUsers.setId(userId);
        updateUsers.setUpdatedTime(new Date());//设置更新的时间
        int i = usersMapper.updateByPrimaryKeySelective(updateUsers);

        return queryUserInfo(userId);
    }
}
