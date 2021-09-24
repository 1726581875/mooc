package cn.edu.lingnan.mooc.message.mapper;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
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
    public void countTest() {

        int msgCount1 = noticeMapper.countUnReadNoticeByUserId(1, 0);

        int msgCount2 = noticeMapper.countUnReadNoticeByManagerId(1,0);

        System.out.println("=======user UnReadMessage count =" + msgCount1);
        System.out.println("=======manager UnReadMessage count =" + msgCount2);
    }


    @Test
    public void findByIdTest() {
        Notice notice = noticeMapper.findById(2);
        System.out.println(notice.getUserType());
    }



}
