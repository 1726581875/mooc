package cn.edu.lingnan.mooc.statistics.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xmz
 * @date: 2021/02/22
 */
@RestController
@RequestMapping("/statistics")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 统计课程分类情况
     * @return
     */
    @GetMapping("/classification")
    public RespResult classificationStatistics(){
        return RespResult.success(statisticsService.getClassificationStatistics());
    }

    /**
     * 课程收藏人数趋势 and 观看人数趋势
     * @param type 时间区间
     * @return
     */
    @GetMapping("/collectionAndView")
    public RespResult collection(@RequestParam(value = "type",defaultValue = "lastWeek") String type){
        return RespResult.success(statisticsService.getCollectionAndViewTrend(type));
    }

    /**
     * 课程
     * @return
     */
    @GetMapping("/view")
    public RespResult view(){
        return RespResult.success(statisticsService.getCourseCollectionStatistics());
    }

}
