package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.core.entity.Role;
import cn.edu.lingnan.core.param.RoLeParam;
import cn.edu.lingnan.core.service.RoleService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/10/23
 */
@RestController
@RequestMapping("/admin/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

   // @Check("role:add")
    @GetMapping("/{id}")
    public RespResult findRoleById(@PathVariable Integer id){
        return RespResult.success(roleService.findById(id));
    }


    /**
     * 分页查询role接口
     * get请求
     * url: /admin/roles/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(Role matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(roleService.findPage(matchObject, pageIndex, pageSize));
    }

    @GetMapping("/all")
    public RespResult findAllRole() {
        return RespResult.success(roleService.findAll());
    }

    /**
     * 更新role接口
     * 请求方法: put
     * url: /admin/roles/{id}
     * @param role
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody Role role) {
        Integer flag = roleService.update(role);
        if (flag == 0) {
            return RespResult.fail("更新Role失败");
        }
        return RespResult.success("更新Role成功");
    }

    /**
     * 插入或更新role
     * 请求方法: post
     * url: /admin/roles/role
     * @param role
     * @return
     */
    @PostMapping("/role")
    public RespResult insertOrUpdate(@RequestBody RoLeParam role) {
        Integer flag = roleService.insertOrUpdate(role);
        if (flag == 0) {
            return RespResult.fail("新增Role失败");
        }
        return RespResult.success("新增Role成功");
    }

    /**
     * 删除role接口
     * 请求方法: delete
     * url: /admin/roles/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = roleService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除Role失败");
        }
        return RespResult.success("删除Role成功");
    }

    /**
     * 批量删除role接口
     * 请求方法: post
     * url: /admin/roles/bacth/delete
     * @param roleIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> roleIdList) {
        roleService.deleteAllByIds(roleIdList);
        return RespResult.success("批量删除Role成功");
    }

}