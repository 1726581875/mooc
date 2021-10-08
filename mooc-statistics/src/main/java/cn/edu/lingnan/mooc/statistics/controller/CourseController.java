package cn.edu.lingnan.mooc.statistics.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.statistics.service.CourseService;
import cn.edu.lingnan.mooc.statistics.service.StatisticsListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xmz
 * @date: 2021/01/02
 */
@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private StatisticsListService statisticsListService;

    /**
     * 根据列获取前10的课程
     * @param field
     * @return
     */
    @RequestMapping("/listTop10")
    public RespResult listTop10CourseByField(@RequestParam(value = "field",defaultValue = "collectionNum") String field
              ,@RequestParam(value = "teacherId",defaultValue = "0") Integer teacherId){

        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取一个月前的时间作为开始时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-1);
        Date beginTime = calendar.getTime();
        //当前时间为结束时间
        Date endTime = new Date();
        log.info("====统计课程排名的时间：{} 至 {}",simpleDateFormat.format(calendar.getTime()),simpleDateFormat.format(endTime));
        return RespResult.success(statisticsListService.listTop10CourseByField(beginTime.getTime(),endTime.getTime(),field, teacherId));
    }


    /**
     * 根据课程关键字搜索
     * @param keyWord
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("/search")
    public RespResult searchCourseByKeyWord(@RequestParam(value = "keyWord",defaultValue = "") String keyWord,
                                            @RequestParam(value = "pageIndex",defaultValue = "0") Integer pageIndex,
                                            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){

        return RespResult.success(courseService.searchCourseByKeyWord(keyWord,pageIndex,pageSize));
    }



}
