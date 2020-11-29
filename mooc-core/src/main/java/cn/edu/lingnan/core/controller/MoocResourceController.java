package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.core.entity.MoocResource;
import cn.edu.lingnan.core.service.MoocResourceService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/10/31
 */
@RestController
@RequestMapping("/admin/moocResources")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class MoocResourceController {

    @Autowired
    private MoocResourceService moocResourceService;

    /**
     * 分页查询moocResource接口
     * get请求
     * url: /admin/moocResources/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(MoocResource matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(moocResourceService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新moocResource接口
     * 请求方法: put
     * url: /admin/moocResources/{id}
     * @param moocResource
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody MoocResource moocResource) {
        Integer flag = moocResourceService.update(moocResource);
        if (flag == 0) {
            return RespResult.fail("更新MoocResource失败");
        }
        return RespResult.success("更新MoocResource成功");
    }

    /**
     * 插入或更新moocResource
     * 请求方法: post
     * url: /admin/moocResources/moocResource
     * @param moocResource
     * @return
     */
    @PostMapping("/moocResource")
    public RespResult insertOrUpdate(@RequestBody MoocResource moocResource) {
        Integer flag = moocResourceService.insertOrUpdate(moocResource);
        if (flag == 0) {
            return RespResult.fail("新增MoocResource失败");
        }
        return RespResult.success("新增MoocResource成功");
    }

    /**
     * 删除moocResource接口
     * 请求方法: delete
     * url: /admin/moocResources/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = moocResourceService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除MoocResource失败");
        }
        return RespResult.success("删除MoocResource成功");
    }

    /**
     * 批量删除moocResource接口
     * 请求方法: post
     * url: /admin/moocResources/bacth/delete
     * @param moocResourceIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> moocResourceIdList) {
        moocResourceService.deleteAllByIds(moocResourceIdList);
        return RespResult.success("批量删除MoocResource成功");
    }

}