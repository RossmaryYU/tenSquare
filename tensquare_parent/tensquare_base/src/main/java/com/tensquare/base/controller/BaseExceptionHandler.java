package com.tensquare.base.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class BaseExceptionHandler {


    /**
     * 异常处理方法
     */
   /* @ExceptionHandler(NullPointerException.class)   //ExceptionHandler: 异常类型处理
    public void handlerError1(){


    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public void handlerError2(){


    }*/

    @ExceptionHandler(Exception.class)  //捕获所有异常
    @ResponseBody
    public Result handlerError(Exception e){
        return new Result(false, StatusCode.ERROR,"异常："+e.getMessage());
    }


}
