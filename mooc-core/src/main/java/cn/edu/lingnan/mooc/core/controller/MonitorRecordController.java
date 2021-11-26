package cn.edu.lingnan.mooc.core.controller;

import cn.edu.lingnan.mooc.core.entity.MonitorRecord;
import cn.edu.lingnan.mooc.core.param.MonitorRecordParam;
import cn.edu.lingnan.mooc.core.service.MonitorRecordService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/01/02
 */
@RestController
@RequestMapping("/admin/monitorRecords")
public class MonitorRecordController {

    @Autowired
    private MonitorRecordService monitorRecordService;


    /**
     * 根据条件分页查询
     * @param monitorRecordParam 条件
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    @GetMapping("/listByCondition")
    public RespResult listByCondition(MonitorRecordParam monitorRecordParam,
                                      @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(monitorRecordService.findPageByCondition(monitorRecordParam, pageIndex, pageSize));
    }

    /**
     * 分页查询monitorRecord接口
     * get请求
     * url: /admin/monitorRecords/list
     * @param matchObject
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult findByPage(MonitorRecord matchObject,
                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return RespResult.success(monitorRecordService.findPage(matchObject, pageIndex, pageSize));
    }

    /**
     * 更新monitorRecord接口
     * 请求方法: put
     * url: /admin/monitorRecords/{id}
     * @param monitorRecord
     * @return
     */
    @PutMapping("/{id}")
    public RespResult update(@RequestBody MonitorRecord monitorRecord) {
        Integer flag = monitorRecordService.update(monitorRecord);
        if (flag == 0) {
            return RespResult.fail("更新MonitorRecord失败");
        }
        return RespResult.success("更新MonitorRecord成功");
    }

    /**
     * 插入或更新monitorRecord
     * 请求方法: post
     * url: /admin/monitorRecords/monitorRecord
     * @param monitorRecord
     * @return
     */
    @PostMapping("/monitorRecord")
    public RespResult insertOrUpdate(@RequestBody MonitorRecord monitorRecord) {
        Integer flag = monitorRecordService.insertOrUpdate(monitorRecord);
        if (flag == 0) {
            return RespResult.fail("新增MonitorRecord失败");
        }
        return RespResult.success("新增MonitorRecord成功");
    }

    /**
     * 删除monitorRecord接口
     * 请求方法: delete
     * url: /admin/monitorRecords/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespResult delete(@PathVariable Integer id) {
        Integer flag = monitorRecordService.deleteById(id);
        if (flag == 0) {
            return RespResult.fail("删除MonitorRecord失败");
        }
        return RespResult.success("删除MonitorRecord成功");
    }

    /**
     * 批量删除monitorRecord接口
     * 请求方法: post
     * url: /admin/monitorRecords/bacth/delete
     * @param monitorRecordIdList
     * @return
     */
    @PostMapping("/batch/delete")
    public RespResult deleteMultiple(@RequestBody List<Integer> monitorRecordIdList) {
        monitorRecordService.deleteAllByIds(monitorRecordIdList);
        return RespResult.success("批量删除MonitorRecord成功");
    }

}