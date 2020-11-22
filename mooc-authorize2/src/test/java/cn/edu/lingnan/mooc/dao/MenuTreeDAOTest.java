package cn.edu.lingnan.mooc.dao;

import cn.edu.lingnan.mooc.model.MenuTree;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@SpringBootTest
public class MenuTreeDAOTest {
    @Autowired
    private MenuTreeDAO menuTreeDAO;

    @Test
    public void findMenuTreeTest(){
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(1);
        roleIds.add(2);
        List<MenuTree> menuList = menuTreeDAO.findMenuList(roleIds);
        Assert.notEmpty(menuList,"查询角色菜单去权限不能为空");
        menuList.forEach(System.out::println);
        System.out.println(menuList.size());
    }

}
