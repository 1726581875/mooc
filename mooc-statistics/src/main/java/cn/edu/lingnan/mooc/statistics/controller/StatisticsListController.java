package cn.edu.lingnan.mooc.statistics.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.statistics.model.vo.StatisticsListViewQuery;
import cn.edu.lingnan.mooc.statistics.service.StatisticsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xmz
 * @date: 2021/04/01
 */
@RestController
@RequestMapping("/statistics")
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
