package com.xuecheng.manage_cms.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomExceptionCacth extends ExceptionCatch {

    static {
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}
