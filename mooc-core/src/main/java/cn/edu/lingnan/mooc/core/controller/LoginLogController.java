package cn.edu.lingnan.mooc.core.controller;
import cn.edu.lingnan.mooc.core.model.param.LoginLogParam;
import cn.edu.lingnan.mooc.core.service.LoginLogService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xmz
 * @date: 2020/11/29
 */
@Slf4j
@RestController
@RequestMapping("/admin/loginLogs")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 分页查询loginLog接口
     * get请求
     * url: /admin/loginLogs/list
     * @param logParam
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(LoginLogParam logParam,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

     return RespResult.success(loginLogService.findLoginLogByCondition(logParam, pageIndex, pageSize));
    }


    @GetMapping("/export")
    public void exportExcel(LoginLogParam logParam, HttpServletResponse response){
        try {
            loginLogService.exportByCondition(logParam,response);
        }catch (Exception e){
            log.error("导出登录日志发生异常：",e);
        }
    }

}