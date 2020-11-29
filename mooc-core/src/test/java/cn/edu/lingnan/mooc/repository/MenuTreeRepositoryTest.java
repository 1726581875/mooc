package cn.edu.lingnan.mooc.repository;

import cn.edu.lingnan.core.entity.MenuTree;
import cn.edu.lingnan.core.repository.MenuTreeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MenuTreeRepositoryTest {

    @Autowired
    private MenuTreeRepository menuTreeRepository;

    @Test
    public void findMenuTreeByRoleId(){
        List<Integer> roleIdList = new ArrayList<>();
        roleIdList.add(1);
        roleIdList.add(2);
        List<MenuTree> menuTreeList = menuTreeRepository.findMenuPermByRoleIds(roleIdList);
        Assert.notEmpty(menuTreeList,"menuTreeList应该有值");
        menuTreeList.forEach(System.out::println);

    }
}