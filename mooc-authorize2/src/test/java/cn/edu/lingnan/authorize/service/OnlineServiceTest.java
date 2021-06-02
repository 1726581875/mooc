package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.model.entity.OnlineUser;
import cn.edu.lingnan.mooc.common.model.PageVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xmz
 * @date: 2020/11/26
 */
@SpringBootTest
public class OnlineServiceTest {

    @Autowired
    private OnlineService onlineService;

    @Test
    public void findOnlineUserByPageTest(){
        PageVO<OnlineUser> userByPage = onlineService.getOnlineUserByPage(1, 10, "");
        System.out.println(userByPage);
    }

}
