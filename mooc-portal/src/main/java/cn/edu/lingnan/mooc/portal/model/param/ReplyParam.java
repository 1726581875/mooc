package cn.edu.lingnan.mooc.portal.model.param;

import lombok.Data;

/**
 * @author xiaomingzhang
 * @date 2021/9/14
 */
@Data
public class ReplyParam {

    private Long courseId;
    private Long commentId;
    private Long replyId;
    private Long userId;
    private Long toUserId;
    private String content;

}
