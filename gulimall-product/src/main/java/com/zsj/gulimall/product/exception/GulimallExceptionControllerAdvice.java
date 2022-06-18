package com.zsj.gulimall.product.exception;

import com.zsj.common.exception.BizCodeEnume;
import com.zsj.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  集中处理所有异常
**/
@Slf4j
@RestControllerAdvice(basePackages = "com.zsj.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {


    @ExceptionHandler(value = MethodArgumentNotValidException.class ) //标注方法可以处理的异常
    public R handleVaildException(MethodArgumentNotValidException e){
        log.error("数据校验出现问题{}，异常类型：{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String,String> emap= new HashMap<>();
        fieldErrors.forEach((item)->{
            String field = item.getField();
            String message = item.getDefaultMessage();
            emap.put(field,message);


        });
        return R.error(BizCodeEnume.VATLD_EXCEPTION.getCode(),BizCodeEnume.VATLD_EXCEPTION.getMsg()).put("data",emap);

    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){

        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }

}
