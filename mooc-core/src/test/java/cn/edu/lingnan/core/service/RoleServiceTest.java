package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.service.RoleService;
import cn.edu.lingnan.core.vo.RoleVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * @author xmz
 * @date: 2020/11/09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Test
    public void findRoleByIdTest(){
        Integer roleId = 1;
        RoleVO roleVO = roleService.findById(roleId);
        Assert.notNull(roleVO,"返回值不能为空");
        Assert.notEmpty(roleVO.getMenuIdList(),"叶子节点不能为空");
        System.out.println(roleVO);
    }

}
