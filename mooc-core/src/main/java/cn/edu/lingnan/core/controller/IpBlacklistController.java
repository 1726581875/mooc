package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.core.entity.IpBlacklist;
import cn.edu.lingnan.core.service.IpBlacklistService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/03/23
 */
@RestController
@RequestMapping("/admin/ipBlacklists")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class IpBlacklistController {

    @Autowired
    private IpBlacklistService ipBlacklistService;

    /**
     * 分页查询ipBlacklist接口
     * get请求
     * url: /admin/ipBlacklists/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(IpBlacklist matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(ipBlacklistService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新ipBlacklist接口
     * 请求方法: put
     * url: /admin/ipBlacklists/{id}
     * @param ipBlacklist
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody IpBlacklist ipBlacklist) {
        Integer flag = ipBlacklistService.update(ipBlacklist);
        if (flag == 0) {
            return RespResult.fail("更新IpBlacklist失败");
        }
        return RespResult.success("更新IpBlacklist成功");
    }

    /**
     * 插入或更新ipBlacklist
     * 请求方法: post
     * url: /admin/ipBlacklists/ipBlacklist
     * @param ipBlacklist
     * @return
     */
    @PostMapping("/ipBlacklist")
    public RespResult insertOrUpdate(@RequestBody IpBlacklist ipBlacklist) {
        Integer flag = ipBlacklistService.insertOrUpdate(ipBlacklist);
        if (flag == 0) {
            return RespResult.fail("新增IpBlacklist失败");
        }
        return RespResult.success("新增IpBlacklist成功");
    }

    /**
     * 删除ipBlacklist接口
     * 请求方法: delete
     * url: /admin/ipBlacklists/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = ipBlacklistService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除IpBlacklist失败");
        }
        return RespResult.success("删除IpBlacklist成功");
    }

    /**
     * 批量删除ipBlacklist接口
     * 请求方法: post
     * url: /admin/ipBlacklists/bacth/delete
     * @param ipBlacklistIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> ipBlacklistIdList) {
        ipBlacklistService.deleteAllByIds(ipBlacklistIdList);
        return RespResult.success("批量删除IpBlacklist成功");
    }

}