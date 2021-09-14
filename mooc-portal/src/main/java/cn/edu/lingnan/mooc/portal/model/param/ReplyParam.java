package cn.edu.lingnan.mooc.portal.model.param;

import lombok.Data;

/**
 * @author xiaomingzhang
 * @date 2021/9/14
 */
@Data
public class ReplyParam {

    private Integer courseId;
    private Integer commentId;
    private Integer replyId;
    private Integer userId;
    private Integer toUserId;
    private String content;

}
