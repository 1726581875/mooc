package cn.edu.lingnan.core.util;

import cn.edu.lingnan.core.entity.Course;
import cn.edu.lingnan.core.entity.MonitorRecord;
import cn.edu.lingnan.core.service.MonitorRecordService;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author xmz
 * @date 2021/01/13
 * 监控记录工具类
 */
public class MonitorRecordUtil {


    private static MonitorRecordService monitorRecordService = SpringContextHolder.getBean(MonitorRecordService.class);


    /**
     * 插入课程监控记录
     */
    public static void insertMonitorRecord(Course newCourse){

        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setCourseId(newCourse.getId());
        monitorRecord.setRecordType("新增课程");
        monitorRecord.setCreateTime(newCourse.getCreateTime());
        monitorRecord.setMessage("新增了课程《" + newCourse.getName() +"》");
        monitorRecord.setTeacherId(newCourse.getTeacherId());
        monitorRecord.setIp("127.0.0.1");
        monitorRecordService.insert(monitorRecord);
    }





}
