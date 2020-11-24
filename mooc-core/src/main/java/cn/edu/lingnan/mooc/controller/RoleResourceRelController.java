package cn.edu.lingnan.mooc.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.service.RoleResourceRelService;
import cn.edu.lingnan.mooc.entity.RoleResourceRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/10/31
 */
@RestController
@RequestMapping("/admin/roleResourceRels")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class RoleResourceRelController {

    @Autowired
    private RoleResourceRelService roleResourceRelService;

    /**
     * 分页查询roleResourceRel接口
     * get请求
     * url: /admin/roleResourceRels/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(RoleResourceRel matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(roleResourceRelService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新roleResourceRel接口
     * 请求方法: put
     * url: /admin/roleResourceRels/{id}
     * @param roleResourceRel
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody RoleResourceRel roleResourceRel) {
        Integer flag = roleResourceRelService.update(roleResourceRel);
        if (flag == 0) {
            return RespResult.fail("更新RoleResourceRel失败");
        }
        return RespResult.success("更新RoleResourceRel成功");
    }

    /**
     * 插入或更新roleResourceRel
     * 请求方法: post
     * url: /admin/roleResourceRels/roleResourceRel
     * @param roleResourceRel
     * @return
     */
    @PostMapping("/roleResourceRel")
    public RespResult insertOrUpdate(@RequestBody RoleResourceRel roleResourceRel) {
        Integer flag = roleResourceRelService.insertOrUpdate(roleResourceRel);
        if (flag == 0) {
            return RespResult.fail("新增RoleResourceRel失败");
        }
        return RespResult.success("新增RoleResourceRel成功");
    }

    /**
     * 删除roleResourceRel接口
     * 请求方法: delete
     * url: /admin/roleResourceRels/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = roleResourceRelService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除RoleResourceRel失败");
        }
        return RespResult.success("删除RoleResourceRel成功");
    }

    /**
     * 批量删除roleResourceRel接口
     * 请求方法: post
     * url: /admin/roleResourceRels/bacth/delete
     * @param roleResourceRelIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> roleResourceRelIdList) {
        roleResourceRelService.deleteAllByIds(roleResourceRelIdList);
        return RespResult.success("批量删除RoleResourceRel成功");
    }

}