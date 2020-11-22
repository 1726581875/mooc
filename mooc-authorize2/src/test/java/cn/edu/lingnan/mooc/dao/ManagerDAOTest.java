package cn.edu.lingnan.mooc.dao;

import cn.edu.lingnan.mooc.model.MoocManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@SpringBootTest
@Transactional
public class ManagerDAOTest {

    @Autowired
    private ManagerDAO managerDAO;
    @Autowired
    private MenuTreeDAO menuTreeDAO;

    @Test
    public void findMoocManagerTest(){
        MoocManager xmz = managerDAO.findManagerByAccount("xmz1");
        System.out.println(xmz);
        menuTreeDAO.findMenuList(Arrays.asList(new Integer[]{1,2,3}));
    }
}
