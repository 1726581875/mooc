package cn.edu.lingnan.mooc.statistics.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/classification")
    public RespResult classificationStatistics(){
        return RespResult.success(statisticsService.getClassificationStatistics());
    }


}
