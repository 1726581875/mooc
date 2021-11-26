package cn.edu.lingnan.mooc.core.repository;
import cn.edu.lingnan.mooc.core.entity.LoginLog;
import cn.edu.lingnan.mooc.core.util.ConvertTimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author xmz
 * @date: 2020/12/01
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginLogRepositoryTest {

    @Resource
    private LoginLogRepository loginLogRepository;

    @Test
    public void findLoginLogByConditionTest(){
        Date time = ConvertTimeUtil.getTime("2020-12-01 12:51:04");
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC,"create_time");
        Page<LoginLog> logPage = loginLogRepository
                .findLoginLogByCondition("登录日志",time,new Date(),pageable);
        System.out.println(logPage.getTotalPages());
        System.out.println();
        System.out.println(logPage.getContent());
        System.out.println(logPage.getTotalElements());

    }




}
