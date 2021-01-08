package cn.edu.lingnan.mooc.statistics.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.statistics.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2021/01/07
 * 监控中心图表数据获取
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    /**
     * 统计一周前/一月前新增课程数
     * @param type week or month
     * @return
     */
    @GetMapping("/countNewCourse")
    public RespResult getCourseCount(@RequestParam(value = "type", defaultValue = "week") String type) {
        Map<String, Long> countMap = null;
        if ("week".equals(type)) {
            countMap = monitorService.countWeekCourse();
        } else {
            countMap = monitorService.countMonthCourse();
        }
        return RespResult.success(countMap);
    }


    /**
     * 统计人员 一周前/一月前新增用户数
     * @param type
     * @return
     */
    public RespResult getUserCount(@RequestParam(value = "type", defaultValue = "week") String type){
        Map<String, Long> countMap = null;
        if ("week".equals(type)) {
            countMap = monitorService.countWeekCourse();
        } else {
            countMap = monitorService.countMonthCourse();
        }
        return RespResult.success(countMap);
    }




}
