package cn.edu.lingnan.core.controller;

import cn.edu.lingnan.core.BaseMvcTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xmz
 * @date: 2020/10/11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SectionControllerTest extends BaseMvcTest {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试分页查找接口
     * 请求方法 get
     * url /admin/sections/list
     */
    @Test
    public void whenQuerySectionListSuccess() throws Exception {
        // 不传条件
        sendGet("/admin/sections/list",null).isOk();

        //传条件
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("id","1");
        ResultActions resultActions = sendGet("/admin/sections/list", paramMap).isOk().getResultActions();
        // 期望只返回一个
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.content.length()").value(1));
    }
    /**
     * 测试删除接口
     * 请求方法 delete
     * url /admin/sections/{id}
     */
    @Test
    public void whenDropSectionSuccess(){
        sendDelete("/admin/sections/1",null).isOk();
    }

    /**
     * 测试更新数据接口
     * 请求方法 put
     * url /admin/sections/{id}
     */
    @Test
    public void whenUpdateSuccess(){
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("id","1");
        //...自己加其他参数
        sendPut("/admin/sections/admin",paramMap).isOk();
    }

    /**
     * 测试更新/插入接口
     * 请求方法 post
     * url /admin/sections/section
     */
    @Test
    public void whenInsertOrUpdateSuccess(){
        // 插入
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("courseId","2");
        paramMap.put("name","测试章节222");
        sendPost("/admin/sections/section",paramMap).isOk();
        // 更新
        Map<String,String> updateParamMap = new HashMap<>();
        updateParamMap.put("id","1");
        //...自己加其他参数
        sendPost("/admin/sections/section",updateParamMap).isOk();
    }

    /**
     * 批量删除的接口
     * 请求方法 post
     * url /admin/sections/section
     */
    @Test
    public void whenBatchDeleteSuccess(){
        List<Integer> sectionIdList = new ArrayList<>();
        sectionIdList.add(1);
        sectionIdList.add(2);
        sectionIdList.add(3);

        sendPost("/admin/sections/batch/delete",sectionIdList).isOk().printMsg();

    }

}
