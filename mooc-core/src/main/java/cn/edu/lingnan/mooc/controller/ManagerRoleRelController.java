package cn.edu.lingnan.mooc.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.service.ManagerRoleRelService;
import cn.edu.lingnan.mooc.entity.ManagerRoleRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/10/23
 */
@RestController
@RequestMapping("/admin/managerRoleRels")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class ManagerRoleRelController {

    @Autowired
    private ManagerRoleRelService managerRoleRelService;

    /**
     * 分页查询managerRoleRel接口
     * get请求
     * url: /admin/managerRoleRels/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(ManagerRoleRel matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(managerRoleRelService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新managerRoleRel接口
     * 请求方法: put
     * url: /admin/managerRoleRels/{id}
     * @param managerRoleRel
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody ManagerRoleRel managerRoleRel) {
        Integer flag = managerRoleRelService.update(managerRoleRel);
        if (flag == 0) {
            return RespResult.fail("更新ManagerRoleRel失败");
        }
        return RespResult.success("更新ManagerRoleRel成功");
    }

    /**
     * 插入或更新managerRoleRel
     * 请求方法: post
     * url: /admin/managerRoleRels/managerRoleRel
     * @param managerRoleRel
     * @return
     */
    @PostMapping("/managerRoleRel")
    public RespResult insertOrUpdate(@RequestBody ManagerRoleRel managerRoleRel) {
        Integer flag = managerRoleRelService.insertOrUpdate(managerRoleRel);
        if (flag == 0) {
            return RespResult.fail("新增ManagerRoleRel失败");
        }
        return RespResult.success("新增ManagerRoleRel成功");
    }

    /**
     * 删除managerRoleRel接口
     * 请求方法: delete
     * url: /admin/managerRoleRels/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = managerRoleRelService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除ManagerRoleRel失败");
        }
        return RespResult.success("删除ManagerRoleRel成功");
    }

    /**
     * 批量删除managerRoleRel接口
     * 请求方法: post
     * url: /admin/managerRoleRels/bacth/delete
     * @param managerRoleRelIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> managerRoleRelIdList) {
        managerRoleRelService.deleteAllByIds(managerRoleRelIdList);
        return RespResult.success("批量删除ManagerRoleRel成功");
    }

}