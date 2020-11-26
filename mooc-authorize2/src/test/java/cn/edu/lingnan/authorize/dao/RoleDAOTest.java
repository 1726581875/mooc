package cn.edu.lingnan.authorize.dao;

import cn.edu.lingnan.authorize.dao.RoleDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@SpringBootTest
public class RoleDAOTest {

    @Autowired
    private RoleDAO roleDAO;
    @Test
    public void findRoleIdListTest(){
         roleDAO.findAllRoleIdByManagerId(1L).forEach(System.out::println);
    }

    @Test
    public void findRoleListTest(){
        roleDAO.findAllRoleByManagerId(1L).forEach(System.out::println);
    }

}
