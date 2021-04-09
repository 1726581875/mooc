package cn.edu.lingnan.mooc.message.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@Accessors(chain = true)
public class Notice {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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
