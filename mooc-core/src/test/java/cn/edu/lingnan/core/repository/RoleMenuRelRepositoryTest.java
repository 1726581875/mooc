package cn.edu.lingnan.core.repository;

import cn.edu.lingnan.core.repository.RoleMenuRelRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class RoleMenuRelRepositoryTest {

    @Autowired
    private RoleMenuRelRepository roleMenuRelRepository;

    /**
     * 测试删除权限
     */
    @Test
    public void deleteAllRoleMenuRelByRoleId(){
        List<Integer> roleIdList = new ArrayList<>();
        roleIdList.add(2);
        roleIdList.add(1);
        roleMenuRelRepository.deleteAllByRoleIdIn(roleIdList);
    }


}
