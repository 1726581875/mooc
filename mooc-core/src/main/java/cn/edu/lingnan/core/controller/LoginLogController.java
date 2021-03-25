package cn.edu.lingnan.core.controller;
import cn.edu.lingnan.core.entity.LoginLog;
import cn.edu.lingnan.core.model.export.LoginLogExport;
import cn.edu.lingnan.core.param.LoginLogParam;
import cn.edu.lingnan.core.service.LoginLogService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import com.alibaba.excel.EasyExcel;
import com.sun.deploy.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/29
 */
@Slf4j
@RestController
@RequestMapping("/admin/loginLogs")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
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