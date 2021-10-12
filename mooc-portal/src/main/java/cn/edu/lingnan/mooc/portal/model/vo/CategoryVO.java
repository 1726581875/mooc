package cn.edu.lingnan.mooc.portal.model.vo;

import cn.edu.lingnan.mooc.portal.model.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/05
 */
@Data
@ToString
public class CategoryVO {

    private Integer id;

    private String name;

    private String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 标签list
     */
    private List<Tag> tagList;

}
