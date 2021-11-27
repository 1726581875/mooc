package cn.edu.lingnan.mooc.core.controller;

import cn.edu.lingnan.mooc.core.entity.Tag;
import cn.edu.lingnan.mooc.core.service.TagService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/05
 */
@RestController
@RequestMapping("/admin/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 分页查询tag接口
     * get请求
     * url: /admin/tags/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(Tag matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(tagService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新tag接口
     * 请求方法: put
     * url: /admin/tags/{id}
     * @param tag
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody Tag tag) {
        Integer flag = tagService.update(tag);
        if (flag == 0) {
            return RespResult.fail("更新Tag失败");
        }
        return RespResult.success("更新Tag成功");
    }

    /**
     * 插入或更新tag
     * 请求方法: post
     * url: /admin/tags/tag
     * @param tag
     * @return
     */
    @PostMapping("/tag")
    public RespResult insertOrUpdate(@RequestBody Tag tag) {
        Integer flag = tagService.insertOrUpdate(tag);
        if (flag == 0) {
            return RespResult.fail("新增Tag失败");
        }
        return RespResult.success("新增Tag成功");
    }

    /**
     * 删除tag接口
     * 请求方法: delete
     * url: /admin/tags/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Long id) {
        Integer flag = tagService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除Tag失败");
        }
        return RespResult.success("删除Tag成功");
    }

    /**
     * 批量删除tag接口
     * 请求方法: post
     * url: /admin/tags/bacth/delete
     * @param tagIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Long> tagIdList) {
        tagService.deleteAllByIds(tagIdList);
        return RespResult.success("批量删除Tag成功");
    }

}