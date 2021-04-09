package cn.edu.lingnan.mooc.message.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeVO {

    private Integer id;

    private Integer sendId;

    private Integer acceptId;

    private Integer courseId;

    private Integer replyId;

    private String content;

    private Integer noticeType;

    private Integer noticeFlag;

    private Date updateTime;

    private Date createTime;

}
