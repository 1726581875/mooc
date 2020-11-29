package cn.edu.lingnan.mooc.repository;

import cn.edu.lingnan.core.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xmz
 * @date: 2020/11/11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void findRoleByManagerIdList(){
        roleRepository.findAllRoleInManagerId(1).forEach(e -> System.out.println(e));
    }

}
