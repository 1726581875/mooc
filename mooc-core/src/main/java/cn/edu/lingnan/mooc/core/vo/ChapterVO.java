package cn.edu.lingnan.mooc.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/19
 */
@Data
@ToString
public class ChapterVO {

    /**
     * 大章id
     */
    private Long id;
    /*
     * 对应课程id
     */
    private Long courseId;
    /*
     * 课程名称
     */
    private String courseName;
    /**
     * 章节名称
     */
    private String name;
    // 大章总时长，单位s 秒
    private Integer duration;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 小节List
     */
    private List<SectionVO> sectionList;

    private boolean hasSection = true;

}
