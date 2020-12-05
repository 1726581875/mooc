package cn.edu.lingnan.core.controller;
import cn.edu.lingnan.core.entity.LoginLog;
import cn.edu.lingnan.core.param.LoginLogParam;
import cn.edu.lingnan.core.service.LoginLogService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/29
 */
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

}