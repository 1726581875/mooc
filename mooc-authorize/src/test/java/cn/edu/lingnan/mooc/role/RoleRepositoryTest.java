package cn.edu.lingnan.mooc.role;

import cn.edu.lingnan.mooc.model.MenuTree;
import cn.edu.lingnan.mooc.repository.MenuTreeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/18
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RoleRepositoryTest {

    @Resource
    private MenuTreeRepository menuTreeRepository;

    @Test
    public void findRoleTest(){
        List<Integer> roleIdList = new ArrayList<>();
        roleIdList.add(100000);
        List<MenuTree> menuPermByRoleIds = menuTreeRepository.findMenuPermByRoleIds(roleIdList);
        System.out.println("menuTreeList=" + menuPermByRoleIds);
    }
}
