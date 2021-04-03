package cn.edu.lingnan.mooc.statistics.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.statistics.entity.StatisticsListViewQuery;
import cn.edu.lingnan.mooc.statistics.service.StatisticsListService;
import cn.edu.lingnan.mooc.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author xmz
 * @date: 2021/04/01
 */
@RestController
@RequestMapping("/statistics")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class StatisticsListController {

    @Autowired
    private StatisticsListService statisticsListService;


    /**
     * 查询课程统计报表
     * @param statisticsListViewQuery
     * @return
     */
    @PostMapping("/sendMessagePage")
    public Object sendMessagePage(@RequestBody StatisticsListViewQuery statisticsListViewQuery) {

        return RespResult.success(statisticsListService.getCourseStatisticsList(statisticsListViewQuery));
    }


}
