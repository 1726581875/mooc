package cn.edu.lingnan.mooc.core.controller;

import cn.edu.lingnan.mooc.core.BaseMvcTest;
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
 * @date: 2020/10/23
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MoocUserControllerTest extends BaseMvcTest {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试分页查找接口
     * 请求方法 get
     * url /admin/moocusers/list
     */
    @Test
    public void whenQueryMoocUserListSuccess() throws Exception {
        // 不传条件
        sendGet("/admin/moocusers/list",null).isOk();

        //传条件
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("id","1");
        ResultActions resultActions = sendGet("/admin/moocusers/list", paramMap).isOk().getResultActions();
        // 期望只返回一个
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.content.length()").value(1));
    }
    /**
     * 测试删除接口
     * 请求方法 delete
     * url /admin/moocusers/{id}
     */
    @Test
    public void whenDropMoocUserSuccess(){
        sendDelete("/admin/moocusers/1",null).isOk();
    }

    /**
     * 测试更新数据接口
     * 请求方法 put
     * url /admin/moocusers/{id}
     */
    @Test
    public void whenUpdateSuccess(){
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("id","1");
        //...自己加其他参数
        sendPut("/admin/moocusers/admin",paramMap).isOk();
    }

    /**
     * 测试更新/插入接口
     * 请求方法 post
     * url /admin/moocusers/moocuser
     */
    @Test
    public void whenInsertOrUpdateSuccess(){
        // 插入
        Map<String,String> paramMap = new HashMap<>();
        //...自己加其他参数
        //有些数据库不为null字段，暂时不能生成
        sendPost("/admin/moocusers/moocuser",paramMap).isOk();
        // 更新
        Map<String,String> updateParamMap = new HashMap<>();
        updateParamMap.put("id","1");
        //...自己加其他参数
        sendPost("/admin/moocusers/moocuser",updateParamMap).isOk();
    }

    /**
     * 批量删除的接口
     * 请求方法 post
     * url /admin/moocusers/moocuser
     */
    @Test
    public void whenBatchDeleteSuccess(){
        List<Integer> moocuserIdList = new ArrayList<>();
        moocuserIdList.add(1);
        moocuserIdList.add(2);
        moocuserIdList.add(3);

        sendPost("/admin/moocusers/batch/delete",moocuserIdList).isOk().printMsg();

    }

}
