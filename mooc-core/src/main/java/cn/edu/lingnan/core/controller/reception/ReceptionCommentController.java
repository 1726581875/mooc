package cn.edu.lingnan.core.controller.reception;

import cn.edu.lingnan.core.entity.CommentReply;
import cn.edu.lingnan.core.service.CommentService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

    /**
     * 查看评论列表
     * @param courseId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult listCommentByCourseId(Integer courseId,
                                            @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                                            @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        return RespResult.success(commentService.findAllCommentByCourseId(courseId,pageIndex,pageSize));
    }

    /**
     * 插入一条评论
     * @param courseId
     * @param commentId
     * @param userId
     * @param toUserId
     * @param content
     * @return
     */
    @PostMapping("/insert")
    public Object insertComment(@RequestParam("courseId") Integer courseId,
                                @RequestParam("commentId") Integer commentId,
                                @RequestParam("userId") Integer userId,
                                @RequestParam("toUserId") Integer toUserId,
                                @RequestParam("content") String content){
        //入参校验
        if(StringUtils.isEmpty(content) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(courseId)){
            return RespResult.parameterError();
        }
        boolean success = commentService.insertCommentOrReply(courseId, commentId, userId, toUserId, content);
        return success ? RespResult.success() : RespResult.failUnKnownError();
    }



}
