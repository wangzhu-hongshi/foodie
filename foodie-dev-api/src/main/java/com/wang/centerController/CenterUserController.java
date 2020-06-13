package com.wang.centerController;

import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;

import com.imooc.utils.JsonUtils;
import com.wang.bo.center.CenterUserBO;
import com.wang.centerService.CenterUserService;
import com.wang.controller.BaseController;
import com.wang.pojo.Users;
import com.wang.resources.FileUpload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.asm.IModelFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息相关接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {
    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;


    @ApiOperation(value = "用户头像上传",notes = "用户头像上传",httpMethod = "POST")
    @PostMapping("uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "file",value = "用户头像",required = true)
                    MultipartFile file,  //接收的文件
            HttpServletRequest request,
            HttpServletResponse response){
        if(StringUtil.isEmpty(userId)){
            return IMOOCJSONResult.errorMsg("userId 不能为空");
        }
        //定义头像保存地址
//        String fileSpace= IMAGE_USER_FACE_LOCATION;
        String fileSpace=fileUpload.getImageUserFaceLocation();
        //在路径上为每个用户增加一个userId 用于区分不同用户
        String uploadPathPrefix= File.separator+userId;

        //文件开始上传
        if(file != null){
            FileOutputStream fileOutputStream=null;
            InputStream inputStream=null;
            try{
                //1 获得文件上传的文件名  并且 重组文件名
                String filename = file.getOriginalFilename();
                if(StringUtils.isNotBlank(filename)){
                    String[] split = filename.split("\\.");
                    //获取文件的后缀名
                    String suffix = split[split.length - 1];
                    //校验图片格式 非常重要
                    if(!suffix.equalsIgnoreCase("png") &&
                    !suffix.equalsIgnoreCase("jpg") &&
                    !suffix.equalsIgnoreCase("jpeg")){
                        return IMOOCJSONResult.errorMsg("图片格式不正确");
                    }
                    //文件名重组
                    String newFileName="face-"+userId+"."+suffix;

                    //上传头像的最终保存位置
                    String finalFacePath=fileSpace+uploadPathPrefix+File.separator+newFileName;

                    uploadPathPrefix+=("/"+newFileName);

                    File outFile=new File(finalFacePath);
                    if(outFile.getParentFile() != null){
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    //文件输出保存到文件夹里
                     fileOutputStream=new FileOutputStream(finalFacePath);
                     inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);

                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(fileOutputStream!=null){
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }else {
            return IMOOCJSONResult.errorMap("文件不能为空");
        }
        //获取图片服务器地址
        String imageServiceUrl = fileUpload.getImageServiceUrl();
        //加上 时间戳 解决 浏览器缓存的问题
        String finalUserFaceUrl=imageServiceUrl+uploadPathPrefix+"?t="+ DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        //更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);
        userResult=setNullProperty(userResult);
        //更新 cookie中的数据
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);

        //TODO 后续要改  增加令牌 token 会整合redis 分布式会话
        return IMOOCJSONResult.ok();


    }
    @ApiOperation(value = "根据用户id 修改用户接口",notes = "根据用户id修改用户接口",httpMethod = "POST")
    @PostMapping("update")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,//接收 校验的错误信息
            HttpServletRequest request,
            HttpServletResponse response){
        if(StringUtil.isEmpty(userId)){
            return IMOOCJSONResult.errorMsg("userId 不能为空");
        }
        if (result.hasErrors()){
            Map<String, String> errors = getErrors(result);
            return IMOOCJSONResult.errorMap(errors);
        }
        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);

        userResult=setNullProperty(userResult);
        //更新 cookie中的数据
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);

        //TODO 后续要改  增加令牌 token 会整合redis 分布式会话
        return IMOOCJSONResult.ok();
    }

    /**
     * 把不必要的用户信息 屏蔽掉
     * @param users
     * @return
     */
    private Users setNullProperty(Users users){
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setRealname(null);
        return users;
    }

    /**
     * 封装bo的校验信息
     * @param result
     * @return
     */
    private Map<String,String> getErrors(BindingResult result){
        Map<String,String> map=new HashMap<>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            map.put(field,defaultMessage);
        }
        return map;
    }


}
