package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.core.entity.Section;
import cn.edu.lingnan.core.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/10/13
 */
@RestController
@RequestMapping("/admin/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;



    @PostMapping("/swap")
    public RespResult sortSwap(@RequestParam("id1") Integer id1,@RequestParam("sort1") Integer sort1
            ,@RequestParam("id2") Integer id2,@RequestParam("sort2") Integer sort2) {
        sectionService.sectionSortSwap(id1,sort1,id2,sort2);
        return RespResult.success();
    }

    /**
     * 分页查询section接口
     * get请求
     * url: /admin/sections/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(Section matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(sectionService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 根据大章ID查询大章下的所有小节
     * @param chapterId
     * @return
     */
    @GetMapping("/listByChapter/{chapterId}")
    public RespResult findSectionByChapterId(@PathVariable("chapterId") Integer chapterId) {
        Section condition = new Section();
        condition.setChapterId(chapterId);
        return RespResult.success(sectionService.findAllByCondition(condition));
    }


    /**
     * 更新section接口
     * 请求方法: put
     * url: /admin/sections/{id}
     * @param section
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody Section section) {
        Integer flag = sectionService.update(section);
        if (flag == 0) {
            return RespResult.fail("更新Section失败");
        }
        return RespResult.success("更新Section成功");
    }

    /**
     * 插入或更新section
     * 请求方法: post
     * url: /admin/sections/section
     * @param section
     * @return
     */
    @PostMapping("/section")
    public RespResult insertOrUpdate(@RequestBody Section section) {
        Integer flag = sectionService.insertOrUpdate(section);
        if (flag == 0) {
            return RespResult.fail("新增Section失败");
        }
        return RespResult.success("新增Section成功");
    }

    /**
     * 删除section接口
     * 请求方法: delete
     * url: /admin/sections/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = sectionService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除Section失败");
        }
        return RespResult.success("删除Section成功");
    }

    /**
     * 批量删除section接口
     * 请求方法: post
     * url: /admin/sections/bacth/delete
     * @param sectionIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> sectionIdList) {
        sectionService.deleteAllByIds(sectionIdList);
        return RespResult.success("批量删除Section成功");
    }

}