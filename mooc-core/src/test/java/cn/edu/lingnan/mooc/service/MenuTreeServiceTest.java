package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.vo.MenuTreeVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MenuTreeServiceTest {

    @Autowired
    private MenuTreeService menuTreeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getMenuTreeTest() throws JsonProcessingException {
        MenuTreeVO menuTreeVO = menuTreeService.getMenuTree();
        String jsonMenuTreeVO = objectMapper.writeValueAsString(menuTreeVO);
        System.out.println(jsonMenuTreeVO);
    }




}
