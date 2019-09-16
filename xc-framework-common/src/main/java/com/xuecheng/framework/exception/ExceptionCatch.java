package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionCatch {
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException o) {
        ResultCode resultCode = o.getResultCode();
        ResponseResult responseResult = new ResponseResult(resultCode);
        return responseResult;
    }
}
