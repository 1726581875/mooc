package cn.edu.lingnan.mooc.common.exception.handler;

import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xiaomingzhang
 * @date 2021/9/10
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = MoocException.class)
    public RespResult handleMoocException(MoocException exception){
        log.error("=====> 捕获了一个MoocException", exception);
        return RespResult.fail(exception.getCode(), exception.getMsg());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RespResult handleException(Exception exception){
        log.error("=====> 捕获了一个Exception", exception);
        return RespResult.failUnKnownError();
    }


}
