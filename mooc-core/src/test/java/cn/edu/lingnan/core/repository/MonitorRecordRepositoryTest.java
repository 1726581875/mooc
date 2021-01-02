package cn.edu.lingnan.core.repository;

import cn.edu.lingnan.core.entity.MonitorRecord;
import cn.edu.lingnan.core.util.ConvertTimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/01/02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MonitorRecordRepositoryTest {
    @Autowired
    private MonitorRecordRepository monitorRecordRepository;

    /**
     * 测试监控记录自定义的分页查询dao方法
     */
    @Test
    public void findMonitorRecordByCondition(){
        Date time = ConvertTimeUtil.getTime("2021-01-01 23:51:04");
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC,"create_time");

        Page<MonitorRecord> monitorRecordPage = monitorRecordRepository
                .findMonitorRecordByCondition("go",null, time, new Date(), pageable);
        List<MonitorRecord> content = monitorRecordPage.getContent();
        content.forEach(System.out::println);

    }
}
