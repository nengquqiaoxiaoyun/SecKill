package com.huakai.controller;

import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author: huakaimay
 * @since: 2021-08-31
 */
@RestControllerAdvice
public class ExceptionController {


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerException(HttpServletRequest request, Exception ex) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (ex instanceof BussinesssError) {
            BussinesssError bussinesssError = (BussinesssError) ex;
            hashMap.put("errCode", bussinesssError.getErrCode());
            hashMap.put("errMsg",  bussinesssError.getErrMsg());
        } else {
            hashMap.put("errCode", ErrorEnum.UNKNOWN_ERROR.getErrCode());
            hashMap.put("errMsg", ErrorEnum.UNKNOWN_ERROR.getErrMsg());
        }

        System.out.println(ex + ", errMsg: " + hashMap.get("errMsg"));


        return CommonReturnType.create(hashMap, "fail");
    }
}
