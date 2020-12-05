package cn.edu.lingnan.core.vo;

import cn.edu.lingnan.core.entity.Tag;
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
    // ID
    private Integer id;
    // 分类名称
    private String name;
    // 分类描述
    private String description;
    // 创建时间
    private Date createTime;
    /**
     * 标签list
     */
    private List<Tag> tagList;

}
