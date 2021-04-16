package cn.edu.lingnan.mooc.statistics.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.statistics.entity.vo.CourseSearchVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    /**
     * 测试根据课程关键字搜索
     */
    @Test
    public void searcherByKeyWord(){
        PageVO<CourseSearchVO> pageVO = courseService.searchCourseByKeyWord("Spring", 1, 10);
        List<CourseSearchVO> courseSearchVOList = pageVO.getContent();
        courseSearchVOList.forEach(System.out::println);
    }


}
