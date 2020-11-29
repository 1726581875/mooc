package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.core.entity.MoocManager;
import cn.edu.lingnan.core.param.ManagerParam;
import cn.edu.lingnan.core.service.MoocManagerService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/10/23
 */
@RestController
@RequestMapping("/admin/moocManagers")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class MoocManagerController {

    @Autowired
    private MoocManagerService moocManagerService;
    /**
     * 分页查询moocManager接口
     * get请求
     * url: /admin/moocManagers/list
     * @param matchStr 名字或者账号
     * @param pageIndex 页下标
     * @param pageSize 页大小
     * @return
     */

    @GetMapping("/list")
    public RespResult findByPage(String matchStr,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(moocManagerService.findPage(matchStr, pageIndex, pageSize));
    }

    /**
     * 更新moocManager接口
     * 请求方法: put
     * url: /admin/moocManagers/{id}
     * @param moocManager
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody MoocManager moocManager) {
        Integer flag = moocManagerService.update(moocManager);
        if (flag == 0) {
            return RespResult.fail("更新MoocManager失败");
        }
        return RespResult.success("更新MoocManager成功");
    }

    /**
     * 插入或更新moocManager
     * 请求方法: post
     * url: /admin/moocManagers/moocManager
     * @param managerParam
     * @return
     */
    @PostMapping("/moocManager")
    @Transactional
    public RespResult insertOrUpdate(@RequestBody ManagerParam managerParam) throws Exception {
        Integer flag = moocManagerService.insertOrUpdate(managerParam);
        if (flag == 0) {
            return RespResult.fail("新增MoocManager失败");
        }
        return RespResult.success("新增MoocManager成功");
    }

    /**
     * 更新管理员状态
     * @param managerId
     * @param status
     * @return
     * @throws Exception
     */
    @PostMapping("/{id}/status/{status}")
    @Transactional
    public RespResult updateStatus(@PathVariable("id") Integer managerId,
                                     @PathVariable("status") Integer status) throws Exception {
        MoocManager manager = new MoocManager();
        manager.setId(managerId);
        manager.setStatus(status);
        Integer flag = moocManagerService.update(manager);
        if (flag == 0) {
            return RespResult.fail("更新管理员状态失败");
        }
        return RespResult.success("更新管理员状态成功");
    }


    /**
     * 删除moocManager接口
     * 请求方法: delete
     * url: /admin/moocManagers/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = moocManagerService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除MoocManager失败");
        }
        return RespResult.success("删除MoocManager成功");
    }

    /**
     * 批量删除moocManager接口
     * 请求方法: post
     * url: /admin/moocManagers/bacth/delete
     * @param moocManagerIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> moocManagerIdList) {
        moocManagerService.deleteAllByIds(moocManagerIdList);
        return RespResult.success("批量删除MoocManager成功");
    }

}