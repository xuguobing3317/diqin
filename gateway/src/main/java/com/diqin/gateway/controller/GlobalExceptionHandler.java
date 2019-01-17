package com.diqin.gateway.controller;

import com.diqin.gateway.common.ResponseDto;
import com.diqin.gateway.enums.ResponseCodeEnum;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: guobing
 * @Date: 2019-1-16 11:11
 * @Description:
 */
@Data
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * 系统异常处理，比如：404,500
     * @param request
     * @param e
     * @return ResponseDto
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseDto defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        log.error("", e);
        ResponseDto responseDto;
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            responseDto = ResponseDto.doRet(ResponseCodeEnum._404);
        } else {
            responseDto = ResponseDto.doRet(ResponseCodeEnum._500);
        }
        return responseDto;
    }

}
