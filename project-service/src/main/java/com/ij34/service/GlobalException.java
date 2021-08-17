package com.ij34.service;

import com.ij34.model.CustomException;
import com.ij34.model.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalException {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ServerResponse handle(Exception ex) {
        logger.info("[ 全局异常 ] ===============》{0}", ex);
        if (ex instanceof CustomException) {
            CustomException customException = (CustomException) ex;
            return ServerResponse.buildError(customException.getCode(), customException.getMsg());
        }
        return ServerResponse.buildError("系统内部错误!");
    }
}