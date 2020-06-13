package com.wang.service.impl;

import com.imooc.enums.Sex;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import com.wang.bo.UsersBO;
import com.wang.mapper.UsersMapper;
import com.wang.pojo.Users;
import com.wang.service.UserService;
import org.n3r.idworker.Id;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;

    private static final String USER_FACE="http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Boolean queryUsernameIsExist(String username) {
        Example userExample =new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        Users users = usersMapper.selectOneByExample(userExample);
        return users != null ;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UsersBO usersBO) {
        Users user=new Users();
        String userId=sid.nextShort();
        user.setUsername(usersBO.getUsername());
        user.setId(userId);
        try {
            user.setPassword(MD5Utils.getMD5Str(usersBO.getPassword()));//密码加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setNickname(usersBO.getUsername());
        user.setFace(USER_FACE);
        user.setBirthday(DateUtil.convertToDate("1990-01-01"));
        user.setSex(Sex.secret.type);

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);
        return user;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin (String username,String password) {
        Example userExample =new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password",password);
        Users users = usersMapper.selectOneByExample(userExample);
        return users;

    }
}
