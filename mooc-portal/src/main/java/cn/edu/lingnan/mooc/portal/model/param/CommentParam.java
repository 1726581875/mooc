package cn.edu.lingnan.mooc.portal.model.param;

import lombok.Data;

/**
 * @author xiaomingzhang
 * @date 2021/9/14
 */
@Data
public class CommentParam extends ReplyParam {
    /**
     * 评论类型，
     */
    private Integer type;
}
