package com.wang.controller;

import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;

import com.wang.bo.UsersBO;
import com.wang.pojo.Users;
import com.wang.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Api(value = "注册登陆",tags = "注册登陆相关的接口")
@RestController
@RequestMapping("passport")
public class PassportController {
    @Autowired
    private UserService userService;

    final static Logger logger= LoggerFactory.getLogger(PassportController.class);

    @ApiOperation(value = "查看用户名是否存在",notes = "查看用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username){
        logger.info("控制器");
        if (StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        //校验用户是否存在
        Boolean isEaist = userService.queryUsernameIsExist(username);
        if(isEaist){
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        //请求成功 用户名没有重复
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UsersBO usersBO,HttpServletRequest request,HttpServletResponse response){
        String username = usersBO.getUsername();
        String password = usersBO.getPassword();
        String confirmPassword = usersBO.getConfirmPassword();

        //0.判断用户名密码是否为空
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能位空");
        }
        //1.判断用户名是否存在
        Boolean isEaist = userService.queryUsernameIsExist(username);
        if(isEaist){
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        //2.判断密码长度不能少于6位
        if(password.length() < 6 ){
            return IMOOCJSONResult.errorMsg("密码长度不能小于6位");
        }
        //3.判断两次密码是否一致
        if(!StringUtils.equals(password,confirmPassword)){
            return IMOOCJSONResult.errorMsg("两次输入的密码不一致");
        }
        //4.实现注册
        Users users = userService.createUser(usersBO);

        users=setNullProperty(users);//对用户不要必要展示得属性进行设置为null
        //存入cookie
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        return IMOOCJSONResult.ok();

    }

    @ApiOperation(value = "用户登陆", notes = "用户登陆", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UsersBO usersBO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = usersBO.getUsername();
        String password = usersBO.getPassword();

        //判断用户名密码不为空
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return IMOOCJSONResult.errorMsg("用户名和密码不能为空！");
        }
        //1.判断用户名是否存在
        Boolean isEaist = userService.queryUsernameIsExist(username);
        if(!isEaist){
            return IMOOCJSONResult.errorMsg("用户名不存在");
        }
        //判断登陆
        Users users = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if(users==null){
            return IMOOCJSONResult.errorMsg("用户名或密码错误");
        }
        users=setNullProperty(users);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        //TODO 生成Token 存入redis
        //TODO 同步购物车数据

        return IMOOCJSONResult.ok(users);

    }
    public Users setNullProperty(Users users){
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setRealname(null);
        return users;
    }


    @ApiOperation(value = "退出登录", notes = "退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,HttpServletRequest request, HttpServletResponse response){
        CookieUtils.deleteCookie(request,response ,"user");

        //TODO 用户退出登录需要清空购物车
        //TODO 分布式会话需要清空用户数据

        return IMOOCJSONResult.ok();
    }

}
