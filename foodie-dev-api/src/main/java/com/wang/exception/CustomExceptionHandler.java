package com.wang.exception;

import com.imooc.utils.IMOOCJSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 自定义异常处理器
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    //上传超过 500k  捕获异常
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public IMOOCJSONResult handlerMaxUploadFile(MaxUploadSizeExceededException e){
        return IMOOCJSONResult.errorMsg("文件上传大小不能超过500k,请压缩后上传");
    }
}
