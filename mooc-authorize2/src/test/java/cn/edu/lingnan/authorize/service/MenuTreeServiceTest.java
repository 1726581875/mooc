package cn.edu.lingnan.authorize.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xmz
 * @date: 2020/11/28
 */

@SpringBootTest
public class MenuTreeServiceTest {


    @Autowired
    private MenuTreeService menuTreeService;

    @Test
    public void getMenuTree(){
        menuTreeService.getMenuTree(0L).forEach(System.out::println);
    }



    @Test
    public void getPermission(){
        menuTreeService.getPermission(1L).forEach(System.out::println);
    }

    @Test
    public void getAllPermissionMenuTree(){
        menuTreeService.getAllPermissionMenuTree(1L).forEach(System.out::println);
    }


}
