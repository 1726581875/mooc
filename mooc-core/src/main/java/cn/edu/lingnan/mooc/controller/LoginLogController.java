package cn.edu.lingnan.mooc.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.service.LoginLogService;
import cn.edu.lingnan.mooc.model.LoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/10/23
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
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(LoginLog matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(loginLogService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新loginLog接口
     * 请求方法: put
     * url: /admin/loginLogs/{id}
     * @param loginLog
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody LoginLog loginLog) {
        Integer flag = loginLogService.update(loginLog);
        if (flag == 0) {
            return RespResult.fail("更新LoginLog失败");
        }
        return RespResult.success("更新LoginLog成功");
    }

    /**
     * 插入或更新loginLog
     * 请求方法: post
     * url: /admin/loginLogs/loginLog
     * @param loginLog
     * @return
     */
    @PostMapping("/loginLog")
    public RespResult insertOrUpdate(@RequestBody LoginLog loginLog) {
        Integer flag = loginLogService.insertOrUpdate(loginLog);
        if (flag == 0) {
            return RespResult.fail("新增LoginLog失败");
        }
        return RespResult.success("新增LoginLog成功");
    }

    /**
     * 删除loginLog接口
     * 请求方法: delete
     * url: /admin/loginLogs/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = loginLogService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除LoginLog失败");
        }
        return RespResult.success("删除LoginLog成功");
    }

    /**
     * 批量删除loginLog接口
     * 请求方法: post
     * url: /admin/loginLogs/bacth/delete
     * @param loginLogIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> loginLogIdList) {
        loginLogService.deleteAllByIds(loginLogIdList);
        return RespResult.success("批量删除LoginLog成功");
    }

}