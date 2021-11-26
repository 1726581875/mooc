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
 * @date: 2020/10/08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ChapterControllerTest extends BaseMvcTest {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试分页查找接口
     * 请求方法 get
     * url /admin/chapters/list
     */
    @Test
    public void whenQueryChapterListSuccess() throws Exception {
        // 不传条件
        sendGet("/admin/chapters/list",null).isOk();

        //传条件
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("id","1");
        ResultActions resultActions = sendGet("/admin/chapters/list", paramMap).isOk().getResultActions();
        // 期望只返回一个
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.content.length()").value(1));
    }
    /**
     * 测试删除接口
     * 请求方法 delete
     * url /admin/chapters/{id}
     */
    @Test
    public void whenDropChapterSuccess(){
        sendDelete("/admin/chapters/1",null).isOk();
    }

    /**
     * 测试更新数据接口
     * 请求方法 put
     * url /admin/chapters/{id}
     */
    @Test
    public void whenUpdateSuccess(){
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("id","1");
        paramMap.put("courseId","2");
        sendPut("/admin/chapters/admin",paramMap).isOk();
    }

    /**
     * 测试更新/插入接口
     * 请求方法 post
     * url /admin/chapters/chapter
     */
    @Test
    public void whenInsertOrUpdateSuccess(){
        // 插入
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("courseId","2");
        paramMap.put("name","测试章节222");
        sendPost("/admin/chapters/chapter",paramMap).isOk();
        // 更新
        Map<String,String> updateParamMap = new HashMap<>();
        updateParamMap.put("id","1");
        updateParamMap.put("courseId","2");
        updateParamMap.put("name","我大概是傻子");
        sendPost("/admin/chapters/chapter",updateParamMap).isOk();
    }

    /**
     * 批量删除的接口
     * 请求方法 post
     * url /admin/chapters/chapter
     */
    @Test
    public void whenBatchDeleteSuccess(){
        List<Integer> chapterIdList = new ArrayList<>();
        chapterIdList.add(1);
        chapterIdList.add(2);
        chapterIdList.add(3);

        sendPost("/admin/chapters/batch/delete",chapterIdList).isOk().printMsg();

    }

}
