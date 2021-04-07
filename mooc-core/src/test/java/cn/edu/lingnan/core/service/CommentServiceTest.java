package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.vo.CommentAndReplyVO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xmz
 * @date: 2021/04/02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void findCommentByCourseId(){
        Integer courseId = 1;
        Integer pageIndex = 1;
        Integer pageSize = 10;
        Integer type = 0;
        PageVO<CommentAndReplyVO> page = commentService.findAllCommentByCourseId(courseId, type,pageIndex, pageSize);
        System.out.println(page);
    }


}
