package cn.edu.lingnan.mooc.authorize.dao;

import cn.edu.lingnan.mooc.authorize.model.entity.MoocManager;
import cn.edu.lingnan.mooc.common.model.PageVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@SpringBootTest
//@Transactional
public class ManagerDAOTest {

    @Autowired
    private ManagerDAO managerDAO;
    @Autowired
    private MenuTreeDAO menuTreeDAO;

    @Test
    public void findMoocManagerTest(){
        MoocManager xmz = managerDAO.findManagerByAccount("xmz1");
        System.out.println(xmz);
        menuTreeDAO.findMenuList(Arrays.asList(new Long[]{1L,2L,3L}));
    }

    @Test
    public void saveTest(){
        MoocManager manager = new MoocManager();
        manager.setName("肖明章");
        manager.setPassword("123456");
        manager.setStatus(1);
        manager.setAccount("www");
        manager.setId(9L);
        managerDAO.save(manager);

    }

    @Test
    public void findManagePageTest(){
        Integer pageIndex = 1;
        Integer pageSize = 10;
        String queryStr = "";
        PageVO<MoocManager> managePage = managerDAO.findManagePage(queryStr, pageIndex, pageSize);
        System.out.println(managePage);
    }


}
