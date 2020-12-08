package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.core.entity.MoocUser;
import cn.edu.lingnan.core.service.MoocUserService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/07
 */
@RestController
@RequestMapping("/admin/moocUsers")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class MoocUserController {

    @Autowired
    private MoocUserService moocUserService;

    /**
     * 分页查询moocUser接口
     * get请求
     * url: /admin/moocUsers/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(MoocUser matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(moocUserService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新moocUser接口
     * 请求方法: put
     * url: /admin/moocUsers/{id}
     * @param moocUser
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody MoocUser moocUser) {
        Integer flag = moocUserService.update(moocUser);
        if (flag == 0) {
            return RespResult.fail("更新MoocUser失败");
        }
        return RespResult.success("更新MoocUser成功");
    }

    /**
     * 插入或更新moocUser
     * 请求方法: post
     * url: /admin/moocUsers/moocUser
     * @param moocUser
     * @return
     */
    @PostMapping("/moocUser")
    public RespResult insertOrUpdate(@RequestBody MoocUser moocUser) {
        Integer flag = moocUserService.insertOrUpdate(moocUser);
        if (flag == 0) {
            return RespResult.fail("新增MoocUser失败");
        }
        return RespResult.success("新增MoocUser成功");
    }

    /**
     * 删除moocUser接口
     * 请求方法: delete
     * url: /admin/moocUsers/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = moocUserService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除MoocUser失败");
        }
        return RespResult.success("删除MoocUser成功");
    }

    /**
     * 批量删除moocUser接口
     * 请求方法: post
     * url: /admin/moocUsers/bacth/delete
     * @param moocUserIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> moocUserIdList) {
        moocUserService.deleteAllByIds(moocUserIdList);
        return RespResult.success("批量删除MoocUser成功");
    }

}