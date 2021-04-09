package cn.edu.lingnan.mooc.message.mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xmz
 * @date: 2021/04/10
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class NoticeMapperTest {

    @Resource
    private NoticeMapper noticeMapper;

    @Test
    public void countTest(){
        int i = noticeMapper.countUnReadNoticeNum();
        System.out.println("====  count test result========: " + i);

        int msgCount1 = noticeMapper.countUnReadNoticeByUserId(1);

        int msgCount2 = noticeMapper.countUnReadNoticeByManagerId(1);

        System.out.println("=======user UnReadMessage count =" + msgCount1);
        System.out.println("=======manager UnReadMessage count =" + msgCount2);


    }


}
