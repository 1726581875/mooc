package cn.edu.lingnan.mooc.portal.model.dto;

import lombok.Data;

/**
 * @author xiaomingzhang
 * @date 2021/9/14
 */
@Data
public class CommentDTO {

    private Integer courseId;
    private Integer commentId;
    private Integer replyId;
    private Integer userId;
    private Integer toUserId;
    private String content;
    private Integer type;
}
