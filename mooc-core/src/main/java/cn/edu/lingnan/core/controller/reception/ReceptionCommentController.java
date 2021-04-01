package cn.edu.lingnan.core.controller.reception;

import cn.edu.lingnan.core.service.CommentService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xmz
 * @date: 2021/04/02
 */
@RestController
@RequestMapping("/comment")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class ReceptionCommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public RespResult listCommentByCourseId(Integer courseId,
                                            @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                                            @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        return RespResult.success(commentService.findAllCommentByCourseId(courseId,pageIndex,pageSize));
    }



}
