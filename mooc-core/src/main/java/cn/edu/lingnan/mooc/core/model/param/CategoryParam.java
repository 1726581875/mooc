package cn.edu.lingnan.mooc.core.model.param;

import cn.edu.lingnan.mooc.core.model.entity.Tag;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/06
 */
@Data
@ToString
public class CategoryParam {
    // ID
    private Integer id;
    // 分类名称
    private String name;
    // 分类描述
    private String description;
    /**
     * 标签list
     */
    private List<Tag> tagList;

}
