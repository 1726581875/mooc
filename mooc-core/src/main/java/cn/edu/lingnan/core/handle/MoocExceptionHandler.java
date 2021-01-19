package cn.edu.lingnan.core.handle;

import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class MoocExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object courseExceptionHandler(Exception e){
        if(e instanceof MoocException){//如果捕获的异常是自定义异常
            MoocException common = (MoocException) e;
            //给前台传对应信息
            return new RespResult(common.getCode(),common.getMsg(),null);
        }

        log.error("系统异常",e);
        return RespResult.failUnKnownError();
    }



}
