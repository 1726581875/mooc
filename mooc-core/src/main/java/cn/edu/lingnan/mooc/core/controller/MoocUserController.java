package cn.edu.lingnan.mooc.core.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.core.entity.MoocUser;
import cn.edu.lingnan.mooc.core.param.UserParam;
import cn.edu.lingnan.mooc.core.service.MoocUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/07
 */
@RestController
@RequestMapping("/admin/moocUsers")
public class MoocUserController {

    @Autowired
    private MoocUserService moocUserService;


    /**
     * 更新用户·信息
     * @param user
     * @return
     */
    @PostMapping("/update")
    public RespResult updateUser(@RequestBody UserParam user){
        moocUserService.update(user);
        return RespResult.success();
    }



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
     * 根据id获取用户
     * @return
     */
    @GetMapping("/{id}")
    public RespResult findById(@PathVariable Long id) {
        return RespResult.success(moocUserService.findById(id));
    }



    /**
     * 更新moocUser接口
     * 请求方法: put
     * url: /admin/moocUsers/{id}
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody UserParam user) {
        Integer flag = moocUserService.update(user);
        if (flag == 0) {
            return RespResult.fail("更新MoocUser失败");
        }
        return RespResult.success("更新MoocUser成功");
    }

    /**
     * 删除moocUser接口
     * 请求方法: delete
     * url: /admin/moocUsers/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Long id) {
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
    public RespResult deleteMultiple(@RequestBody List<Long> moocUserIdList) {
        moocUserService.deleteAllByIds(moocUserIdList);
        return RespResult.success("批量删除MoocUser成功");
    }

}