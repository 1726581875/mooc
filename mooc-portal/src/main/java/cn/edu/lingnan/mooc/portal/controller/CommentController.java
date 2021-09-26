package cn.edu.lingnan.mooc.portal.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.portal.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author xmz
 * @date: 2021/04/02
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

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
    public RespResult listCommentByCourseId(Long courseId,
                                            @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                                            @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                            @RequestParam("type")Integer type){
        return RespResult.success(commentService.findAllCommentByCourseId(courseId,type,pageIndex,pageSize));
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
    public Object insertComment(@RequestParam("courseId") Long courseId,
                                @RequestParam("commentId") Long commentId,
                                @RequestParam("replyId") Long replyId,
                                @RequestParam("userId") Long userId,
                                @RequestParam("toUserId") Long toUserId,
                                @RequestParam("type") Integer type,
                                @RequestParam("content") String content){
        //入参校验
        if(StringUtils.isEmpty(content) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(courseId)){
            return RespResult.parameterError();
        }
       // boolean success = commentService.insertCommentOrReply(courseId, commentId, replyId, userId, toUserId, content,type);
        return RespResult.success();
    }


    /**
     * 根据用户id查询评论
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/listByUserId")
    public RespResult listCommentByUserId(Long userId,
                                            @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                                            @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                            @RequestParam("type")Integer type){

        return RespResult.success(commentService.findAllCommentByCourseId(userId,type, pageIndex,pageSize));
    }

    /**
     * 查询评论列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/listAll")
    public RespResult listAllComment(@RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                                     @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                     @RequestParam("type")Integer type){

        return RespResult.success(commentService.findAllCommentList(type,pageIndex,pageSize));
    }

    /**
     * 查询评论的回复列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/listCommentDetail")
    public RespResult listCommentReply(@RequestParam(value = "commentId") Long commentId,
                                     @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                                     @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        return RespResult.success(commentService.getCommentDetail(commentId));
    }


}
