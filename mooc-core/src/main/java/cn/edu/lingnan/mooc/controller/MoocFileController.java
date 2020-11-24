package cn.edu.lingnan.mooc.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.service.MoocFileService;
import cn.edu.lingnan.mooc.entity.MoocFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/10/23
 */
@RestController
@RequestMapping("/admin/moocFiles")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class MoocFileController {

    @Autowired
    private MoocFileService moocFileService;

    /**
     * 分页查询moocFile接口
     * get请求
     * url: /admin/moocFiles/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(MoocFile matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(moocFileService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新moocFile接口
     * 请求方法: put
     * url: /admin/moocFiles/{id}
     * @param moocFile
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody MoocFile moocFile) {
        Integer flag = moocFileService.update(moocFile);
        if (flag == 0) {
            return RespResult.fail("更新MoocFile失败");
        }
        return RespResult.success("更新MoocFile成功");
    }

    /**
     * 插入或更新moocFile
     * 请求方法: post
     * url: /admin/moocFiles/moocFile
     * @param moocFile
     * @return
     */
    @PostMapping("/moocFile")
    public RespResult insertOrUpdate(@RequestBody MoocFile moocFile) {
        Integer flag = moocFileService.insertOrUpdate(moocFile);
        if (flag == 0) {
            return RespResult.fail("新增MoocFile失败");
        }
        return RespResult.success("新增MoocFile成功");
    }

    /**
     * 删除moocFile接口
     * 请求方法: delete
     * url: /admin/moocFiles/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = moocFileService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除MoocFile失败");
        }
        return RespResult.success("删除MoocFile成功");
    }

    /**
     * 批量删除moocFile接口
     * 请求方法: post
     * url: /admin/moocFiles/bacth/delete
     * @param moocFileIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> moocFileIdList) {
        moocFileService.deleteAllByIds(moocFileIdList);
        return RespResult.success("批量删除MoocFile成功");
    }

}